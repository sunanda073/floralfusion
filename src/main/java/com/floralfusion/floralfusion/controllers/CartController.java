package com.floralfusion.floralfusion.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.floralfusion.floralfusion.entities.CartItem;
import com.floralfusion.floralfusion.entities.Customer;
import com.floralfusion.floralfusion.entities.Product;
import com.floralfusion.floralfusion.repositories.CartItemRepository;
import com.floralfusion.floralfusion.repositories.CustomerRepository;
import com.floralfusion.floralfusion.repositories.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CustomerRepository customerRepo;

    // Assuming customerId is passed in session or request
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null) {
            return "redirect:/login"; // basic check
        }

        Customer customer = customerRepo.findById(customerId).orElse(null);
        Product product = productRepo.findById(productId).orElse(null);

        if (product != null && customer != null) {
            Optional<CartItem> existingCartItem = cartItemRepo.findByCustomerAndProduct(customer, product);

            CartItem cartItem = existingCartItem.orElse(new CartItem());
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItem.getQuantity() + 1);

            cartItemRepo.save(cartItem);
        }

        return "redirect:/customer"; // or redirect back to same page
    }

    @GetMapping("")
    public String viewCart(Model model, HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");
        Customer customer = customerRepo.findById(customerId).orElse(null);

        List<CartItem> cartItems = cartItemRepo.findByCustomer(customer);

        // Calculate the total price
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // Add the totalPrice to the model
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

}
