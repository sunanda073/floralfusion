// package com.floralfusion.floralfusion.controllers;

// import java.nio.file.Paths;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.ui.Model;

// import com.floralfusion.floralfusion.entities.Company;
// import com.floralfusion.floralfusion.entities.Product;
// import com.floralfusion.floralfusion.repositories.ManufacturerRepository;
// import com.floralfusion.floralfusion.repositories.ProductRepository;
// import com.floralfusion.floralfusion.repositories.UserRepository;

// import jakarta.servlet.http.HttpSession;

// import org.springframework.util.StringUtils;

// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Path;

// @Controller
// public class ProductController {

//     @Autowired
//     private ManufacturerRepository manufacturerRepository;

//     @Autowired
//     private ProductRepository productRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @GetMapping("/add_product.html")
//     public String addProductPage() {
//         return "add_product";
//     }

//     @PostMapping("/addProduct")
//     public String addProduct(
//             @RequestParam("name") String name,
//             @RequestParam("description") String description,
//             @RequestParam("price") double price,
//             @RequestParam("image") MultipartFile image,
//             HttpSession session, // Add HttpSession parameter
//             Model model) {

//         try {
//             // Retrieve logged-in user ID from session
//             String loggedInUserId = (String) session.getAttribute("loggedInUserId");
//             if (loggedInUserId == null) {
//                 model.addAttribute("error", "User not logged in.");
//                 return "add_product";
//             }

//             // Query the manufacturer using the logged-in user ID
//             Company manufacturer = manufacturerRepository.findByUserUserID(loggedInUserId);
//             if (manufacturer == null) {
//                 model.addAttribute("error", "Manufacturer not found for user: " + loggedInUserId);
//                 return "add_product";
//             }

//             // Save the image and get the URL
//             String imageUrl = saveImage(image);

//             // Create Product object
//             Product product = new Product(name, description, manufacturer, price, imageUrl);

//             // Save product details to the database
//             productRepository.save(product);

//             // model.addAttribute("message", "Product added successfully!");
//             // return "redirect:/products"; // Redirect to the product list

//             List<Product> products = productRepository.findByManufacturer(manufacturer);

//             // Add success message and product list to the model
//             model.addAttribute("products", products);
//             model.addAttribute("message", "Product added successfully!");

//             // Stay on the products page without redirecting
//             return "manufacturer_products";

//         } catch (IOException e) {
//             model.addAttribute("error", "Error adding product: " + e.getMessage());
//             e.printStackTrace(); // Print the stack trace to the console for debugging
//             return "add_product";
//         }

//     }

//     public String saveImage(MultipartFile image) throws IOException {
//         // Define the path where you want to store the image
//         String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/productUploads";

//         // Get the original file name and make it unique
//         String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
//         String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;

//         // Ensure the directory exists
//         File directory = new File(uploadDir);
//         if (!directory.exists()) {
//             if (!directory.mkdirs()) {
//                 throw new IOException("Failed to create directory: " + uploadDir);
//             }
//         }

//         // Save the file to the server's file system
//         Path path = Paths.get(uploadDir, uniqueFilename);
//         image.transferTo(path.toFile());

//         // Return the URL (you can adjust this based on your server configuration)
//         return "/images/productUploads/" + uniqueFilename; // Return the path that can be accessed by the browser
//     }

//     @GetMapping("/products")
//     public String viewProducts(HttpSession session, Model model) {
//         // Retrieve logged-in user ID from session
//         String loggedInUserId = (String) session.getAttribute("loggedInUserId");
//         if (loggedInUserId == null) {
//             model.addAttribute("error", "User not logged in.");
//             return "login"; // Redirect to login page if user is not logged in
//         }

//         // Query the manufacturer using the logged-in user ID
//         Company manufacturer = manufacturerRepository.findByUserUserID(loggedInUserId);
//         if (manufacturer == null) {
//             model.addAttribute("error", "Manufacturer not found for user: " + loggedInUserId);
//             return "add_product"; // Redirect to add product page if manufacturer is not found
//         }

//         // Retrieve products associated with the manufacturer
//         List<Product> products = productRepository.findByManufacturer(manufacturer);

//         // Add the list of products to the model
//         model.addAttribute("products", products);
//         return "manufacturer_products"; // Return the view for displaying the products
//     }
// }