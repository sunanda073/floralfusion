package com.floralfusion.floralfusion.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.floralfusion.floralfusion.entities.CartItem;
import com.floralfusion.floralfusion.entities.Company;
import com.floralfusion.floralfusion.entities.CompanyOrder;
import com.floralfusion.floralfusion.entities.Customer;
import com.floralfusion.floralfusion.entities.CustomerOrder;
import com.floralfusion.floralfusion.entities.DeliveryPartner;
import com.floralfusion.floralfusion.entities.FlowerStock;
import com.floralfusion.floralfusion.entities.OrderItem;
import com.floralfusion.floralfusion.entities.Payment;
import com.floralfusion.floralfusion.entities.Product;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.enums.DeliveryStatus;
import com.floralfusion.floralfusion.enums.FlowerType;
import com.floralfusion.floralfusion.enums.OrderStatus;
import com.floralfusion.floralfusion.enums.PaymentStatus;
import com.floralfusion.floralfusion.repositories.CartItemRepository;
import com.floralfusion.floralfusion.repositories.CompanyOrderRepository;
import com.floralfusion.floralfusion.repositories.CompanyRepository;
import com.floralfusion.floralfusion.repositories.CustomerOrderRepository;
import com.floralfusion.floralfusion.repositories.DeliveryPartnerRepository;
import com.floralfusion.floralfusion.repositories.FlowerStockRepository;
import com.floralfusion.floralfusion.repositories.OrderItemRepository;
import com.floralfusion.floralfusion.repositories.PaymentRepository;
import com.floralfusion.floralfusion.repositories.ProductRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private CompanyOrderRepository companyOrderRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Autowired
    private FlowerStockRepository flowerStockRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final String API_KEY = "rzp_test_LWliCpXznGjQeG";
    private static final String API_SECRET = "KbZfLB1xSVwPYIxpXhKCw4Jd";

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data) {
        try {
            double amount = Double.parseDouble(data.get("amount").toString());
            RazorpayClient razorpayClient = new RazorpayClient(API_KEY, API_SECRET);

            JSONObject options = new JSONObject();
            options.put("amount", (int) amount); // already multiplied by 100 on frontend
            options.put("currency", "INR");
            options.put("receipt", "order_receipt_1");

            Order order = razorpayClient.orders.create(options);
            return ResponseEntity.ok(order.toString()); // Send JSON as string

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order");
        }
    }

    @PostMapping("/payment-capture")
    public ResponseEntity<String> capturePayment(
            @RequestBody Map<String, Object> data,
            HttpSession session) {

        try {
            // Extract payment data
            String paymentId = data.get("paymentId").toString();
            String orderId = (String) data.get("orderId");
            String flowerType = (String) data.get("flowerType");
            double quantity = Double.parseDouble(data.get("quantity").toString());
            String deliveryAddress = (String) data.get("deliveryAddress");

            if (paymentId == null || flowerType == null || quantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid parameters.");
            }

            // Get user from session
            User user = (User) session.getAttribute("user");
            if (user == null || !"company".equalsIgnoreCase(user.getRole())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
            }

            Company company = companyRepository.findByUser(user);
            if (company == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Company not found");
            }

            FlowerType flowerTypeEnum = FlowerType.valueOf(flowerType.toUpperCase());

            // Check and update stock
            Optional<FlowerStock> stockOptional = flowerStockRepository.findById(flowerTypeEnum);
            if (stockOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Flower stock not found.");
            }

            FlowerStock flowerStock = stockOptional.get();
            if (flowerStock.getQuantity() < quantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient flower stock.");
            }
            flowerStock.setQuantity(flowerStock.getQuantity() - (int) quantity);
            flowerStockRepository.save(flowerStock);

            double price = quantity * 1.0;

            // Create CompanyOrder
            CompanyOrder order = new CompanyOrder();
            order.setCompany(company);
            order.setQuantityInKg(quantity);
            order.setPrice(price);
            order.setPaymentStatus(PaymentStatus.COMPLETED);
            order.setPaymentCompletedAt(LocalDateTime.now());
            order.setOrderedAt(LocalDateTime.now());
            order.setExpectedDeliveryBy(LocalDateTime.now().plusHours(24));
            order.setDeliveryStatus(DeliveryStatus.PENDING);
            order.setFlowerType(flowerTypeEnum);
            order.setDeliveryAddress(deliveryAddress);

            // Create Payment and associate it
            Payment payment = new Payment();
            payment.setRazorpayPaymentId(paymentId);
            payment.setAmount(price);
            payment.setPaymentMethod("Razorpay");
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setCompanyOrder(order); // Set back-reference

            order.setPayment(payment); // Set payment on order

            // Assign delivery partner
            List<DeliveryPartner> deliveryPartners = deliveryPartnerRepository
                    .findByAreaAndCurrentDeliveriesLessThan(company.getArea(), 5);
            if (deliveryPartners.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No available delivery partner found.");
            }

            DeliveryPartner deliveryPartner = deliveryPartners.get(0);
            order.setDeliveryPartner(deliveryPartner);
            order.setAssignedAt(LocalDateTime.now());

            deliveryPartner.setCurrentDeliveries(deliveryPartner.getCurrentDeliveries() + 1);
            deliveryPartnerRepository.save(deliveryPartner);

            // Save order with cascade saving payment
            companyOrderRepository.save(order);

            return ResponseEntity.ok("Payment Successful. Order saved with ID: " + order.getOrderId());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong.");
        }
    }

    @PostMapping("/customer/payment-capture")
    public ResponseEntity<String> captureCustomerPayment(
            @RequestBody Map<String, Object> data,
            HttpSession session) {
        try {
            // Extract required fields from request body
            String paymentId = (String) data.get("paymentId");
            String orderId = (String) data.get("orderId");
            String shippingAddress = (String) data.get("shippingAddress");
    
            // Validate input
            if (paymentId == null || orderId == null || shippingAddress == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields.");
            }
    
            // Get customer from session
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer not logged in.");
            }
    
            // Fetch cart items
            List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
            if (cartItems.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty.");
            }
    
            // Calculate total amount
            double totalAmount = cartItems.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
    
            // Create and save Payment object first
            Payment payment = new Payment();
            payment.setPaymentMethod("Razorpay");
            payment.setAmount(totalAmount);
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setRazorpayPaymentId(paymentId);
    
            payment = paymentRepository.save(payment);
    
            // Create and save CustomerOrder
            CustomerOrder customerOrder = new CustomerOrder();
            customerOrder.setCustomer(customer);
            customerOrder.setTotalAmount(totalAmount);
            customerOrder.setOrderDate(LocalDateTime.now().toString());
            customerOrder.setOrderStatus(OrderStatus.PROCESSING);
            customerOrder.setPaymentStatus(PaymentStatus.COMPLETED);
            customerOrder.setShippingAddress(shippingAddress);
            customerOrder.setPayment(payment); // link payment
    
            customerOrder = customerOrderRepository.save(customerOrder);
    
            // Update payment with reference to customer order
            payment.setCustomerOrder(customerOrder);
            paymentRepository.save(payment);
    
            // Save each OrderItem and update product stock
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                int currentStock = product.getStockQuantity();
                int orderedQty = cartItem.getQuantity();
    
                if (currentStock < orderedQty) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Insufficient stock for product: " + product.getProductName());
                }
    
                // Create order item
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(customerOrder);
                orderItem.setProduct(product);
                orderItem.setQuantity(orderedQty);
                orderItem.setUnitPrice(product.getPrice());
                orderItemRepository.save(orderItem);
    
                // Update product stock
                product.setStockQuantity(currentStock - orderedQty);
                productRepository.save(product);
            }
    
            // Clear cart
            cartItemRepository.deleteAll(cartItems);
    
            // Respond with JSON (optional: change if frontend prefers plain text)
            String response = String.format("{\"message\": \"Order placed successfully\", \"orderId\": %d}", customerOrder.getOrderID());
            return ResponseEntity.ok(response);
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong.");
        }
    }
    
}