package com.floralfusion.floralfusion.controllers;

import com.floralfusion.floralfusion.entities.Company;
import com.floralfusion.floralfusion.entities.CompanyOrder;
import com.floralfusion.floralfusion.entities.CustomerOrder;
import com.floralfusion.floralfusion.entities.DeliveryPartner;
import com.floralfusion.floralfusion.entities.FlowerStock;
import com.floralfusion.floralfusion.entities.OrderItem;
import com.floralfusion.floralfusion.entities.Product;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.enums.DeliveryStatus;
import com.floralfusion.floralfusion.enums.FlowerType;
import com.floralfusion.floralfusion.enums.PaymentStatus;
import com.floralfusion.floralfusion.enums.ProductCategory;
import com.floralfusion.floralfusion.repositories.CompanyOrderRepository;
import com.floralfusion.floralfusion.repositories.CompanyRepository;
import com.floralfusion.floralfusion.repositories.DeliveryPartnerRepository;
import com.floralfusion.floralfusion.repositories.FlowerStockRepository;
import com.floralfusion.floralfusion.repositories.OrderItemRepository;
import com.floralfusion.floralfusion.services.CompanyOrderService;
import com.floralfusion.floralfusion.services.CompanyService;
import com.floralfusion.floralfusion.services.ProductService;
import com.razorpay.RazorpayClient;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.nio.file.Path;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyOrderRepository companyOrderRepository; // already used in your code

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/company")
    public String showCompanyDashboard(HttpSession session, Model model) {
        // Retrieve the user object from the session
        User user = (User) session.getAttribute("user");

        // Check if user is logged in
        if (user == null) {
            return "redirect:/login?error=unauthorized"; // Redirect if user is not logged in
        }

        // Ensure the user is a company representative
        if (!"company".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login?error=unauthorized"; // Redirect if user is not a company
        }

        // Retrieve the company object using the user
        Company company = companyService.findByUser(user);

        // If company is not found, redirect or handle accordingly
        if (company == null) {
            return "redirect:/login?error=company_not_found"; // Redirect if company is not found
        }

        Long companyId = company.getCompanyID();

        // Fetch all orders for the company
        List<CompanyOrder> orders = companyOrderRepository.findAllByCompany_CompanyID(companyId);

        // Add orders to the model
        model.addAttribute("orders", orders);

        // Set the company ID in the session for future use
        session.setAttribute("companyId", company.getCompanyID());
        session.setAttribute("company", company);

        // Fetch OrderItems that include products of this company
        List<OrderItem> companyOrderItems = orderItemRepository.findByCompanyId(companyId);

        // Extract CustomerOrders from these items (avoid duplicates)
        Set<CustomerOrder> relatedOrders = companyOrderItems.stream()
                .map(OrderItem::getOrder)
                .collect(Collectors.toSet());

        model.addAttribute("orderItems", companyOrderItems);

        // Add attributes to the model for use in the view
        model.addAttribute("user", user);
        model.addAttribute("companyId", company.getCompanyID());
        model.addAttribute("companyName", user.getFname() + " " + user.getLname());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        System.out.println("THIS IS THE COMPANY ID : " + company.getCompanyID());

        // Return the company dashboard view
        return "company_view";
    }

    @PostMapping("/company/upload-product")
    public String uploadProduct(@RequestParam("productName") String productName,
            @RequestParam("category") ProductCategory category,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("productImage") MultipartFile productImage, // Use MultipartFile for image upload
            @RequestParam("stockQuantity") int stockQuantity,
            HttpSession session, RedirectAttributes redirectAttributes) {

        // Handle image upload
        String imageUrl = saveImageToFilesystem(productImage);

        // Check if imageUrl is valid (i.e., the image was uploaded successfully)
        if (imageUrl == null) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image.");
            return "redirect:/company";
        }

        // Retrieve the company from the session (assuming it's stored there)
        Company company = (Company) session.getAttribute("company");

        // Check if company is valid
        if (company == null) {
            redirectAttributes.addFlashAttribute("error", "Company not found.");
            return "redirect:/company";
        }

        // Create and save the Product object, passing the company object
        Product product = new Product(productName, category, description, company, price, imageUrl, stockQuantity);
        productService.saveProduct(product);

        // Redirect to the company dashboard or another appropriate page
        redirectAttributes.addFlashAttribute("success", "Product uploaded successfully!");
        return "redirect:/company";
    }

    private String saveImageToFilesystem(MultipartFile file) {
        // Use the static folder inside the resources directory to save the images
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/images"; // Save in the
                                                                                                         // static
                                                                                                         // folder

        // Ensure the directory exists
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs(); // Create directories if they don't exist
            if (!created) {
                System.err.println("Failed to create upload directory.");
                return null;
            }
        }

        // Get the original file name and define the path to save it
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            System.err.println("Invalid file name: " + originalFilename);
            return null;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Resolve path to ensure proper file path construction across platforms
        Path path = Paths.get(uploadDir).resolve(uniqueFilename);

        try {
            // Save the file to the specified location
            file.transferTo(path.toFile());
        } catch (IOException e) {
            System.err.println("Failed to save file: " + e.getMessage());
            e.printStackTrace();
            return null; // Return null if image upload fails
        }

        // Return the relative path to the image (to be used in the frontend)
        return "/uploads/images/" + uniqueFilename; // Adjusted for the correct URL path
    }

    @GetMapping("/company/my-products")
    public String showProductsPage(HttpSession session, Model model) {
        Long companyId = (Long) session.getAttribute("companyId");
        if (companyId == null) {
            return "redirect:/login?error=unauthorized";
        }

        List<Product> products = productService.findProductsByCompanyId(companyId);
        model.addAttribute("products", products);
        return "company_products";
    }

    @PostMapping("/delete-product")
    public String deleteProduct(@RequestParam Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/company/my-products"; // Redirect back to the My Products page
    }

    @PostMapping("/edit-product")
    public String editProduct(@ModelAttribute Product product) {
        productService.updateProduct(product);
        return "redirect:/company/my-products"; // Redirect back to the My Products page
    }

    
}
