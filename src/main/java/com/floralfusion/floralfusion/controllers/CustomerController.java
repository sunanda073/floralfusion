package com.floralfusion.floralfusion.controllers;

import com.floralfusion.floralfusion.entities.Customer;
import com.floralfusion.floralfusion.entities.CustomerOrder;
import com.floralfusion.floralfusion.entities.Product;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.repositories.CustomerOrderRepository;
import com.floralfusion.floralfusion.repositories.CustomerRepository;
import com.floralfusion.floralfusion.repositories.ProductRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    // @Autowired
    // private CartItemRepository cartItemRepository;

    // Customer dashboard
    @GetMapping("")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !"customer".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        Customer customer = customerRepository.findByUser(user);
        model.addAttribute("customer", customer);
        return "customer_view";
    }

    // View all products
    @GetMapping("/products")
    public String viewProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "customer/products";
    }

    @GetMapping("/orders")
    public String getCustomerOrders(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login"; // If customer is not logged in, redirect to login page
        }

        // Fetch orders based on customer ID
        List<CustomerOrder> orders = customerOrderRepository.findByCustomer(customer);
        model.addAttribute("orders", orders);
        model.addAttribute("customer", customer); 

        return "customer_orders"; // Thymeleaf template for orders
    }

    
}
