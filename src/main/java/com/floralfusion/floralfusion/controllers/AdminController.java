package com.floralfusion.floralfusion.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.floralfusion.floralfusion.entities.Admin;
import com.floralfusion.floralfusion.entities.Collector;
import com.floralfusion.floralfusion.entities.FlowerStock;
import com.floralfusion.floralfusion.repositories.AdminRepository;
import com.floralfusion.floralfusion.services.CollectorService;
import com.floralfusion.floralfusion.services.FlowerStockService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse; 


@Controller
@RequestMapping("/admin") // Base path for all admin routes
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CollectorService collectorService;

    // @GetMapping // Handles GET /admin
    // public String showAdminPage(Model model, @AuthenticationPrincipal Admin
    // admin) {
    // List<Collector> collectors = collectorService.getAllCollectors();
    // model.addAttribute("collectors", collectors);
    // model.addAttribute("admin", admin);
    // return "admin_view";
    // }

    @Autowired
    private FlowerStockService flowerStockService;

    @GetMapping
    public String showAdminPage(Model model, HttpSession session, HttpServletResponse response) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            System.err.println("ADMIN is NULL");
            return "redirect:/admin/login"; // Redirect if not logged in
        }

        // Add headers to prevent browser from caching this page
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        // Fetch the total number of collectors for the logged-in admin
        int totalCollectors = collectorService.countCollectorsByAdmin(admin.getAdminID());

        // Fetch total stock quantity from flower_stock table
        int totalStock = flowerStockService.getTotalStock();

        List<FlowerStock> flowerStockList = flowerStockService.getAllFlowerStock();

        model.addAttribute("totalCollectors", totalCollectors);
        model.addAttribute("totalStock", totalStock);
        model.addAttribute("collectors", collectorService.getAllCollectors());
        model.addAttribute("flowerStockList", flowerStockList);
        model.addAttribute("admin", admin);
        

        return "admin_view";
    }

    @GetMapping("/login") // Handles GET /admin/login
    public String showAdminLoginPage() {
        return "admin_login";
    }

    // @PostMapping("/login") // Handles POST /admin/login
    // public String processLogin(@RequestParam String email,
    // @RequestParam String password,
    // @RequestParam String passkey,
    // Model model) {
    // Optional<Admin> admin =
    // adminRepository.findByUser_EmailAndUser_PasswordAndPasskey(email, password,
    // passkey);

    // if (admin.isPresent()) {
    // System.out.println("Admin found: " + admin.get());
    // return "redirect:/admin"; // Redirect to admin dashboard
    // } else {
    // System.out.println("Admin not found. Check credentials!");
    // model.addAttribute("error", "Invalid credentials");
    // return "admin_login"; // Reload login page with error
    // }
    // }

    @PostMapping("/login") // Handles POST /admin/login
    public String processLogin(@RequestParam String email,
            @RequestParam String password,
            @RequestParam String passkey,
            Model model,
            HttpSession session) { // Add HttpSession parameter
        Optional<Admin> admin = adminRepository.findByUser_EmailAndUser_PasswordAndPasskey(email, password, passkey);

        if (admin.isPresent()) {
            System.out.println("Admin found: " + admin.get());

            // Store admin in session
            session.setAttribute("admin", admin.get());

            return "redirect:/admin"; // Redirect to admin dashboard
        } else {
            System.out.println("Admin not found. Check credentials!");
            model.addAttribute("error", "Invalid credentials");
            return "admin_login"; // Reload login page with error
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/admin/login"; // Redirect to login page (or home)
    }

}
