package com.floralfusion.floralfusion.controllers;

import com.floralfusion.floralfusion.entities.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyController {

    @GetMapping("/company")
    public String showCompanyDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        // Ensure the user is logged in and is a company representative
        if (user == null || !"company".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login?error=unauthorized";
        }

        model.addAttribute("companyName", user.getFname() + " " + user.getLname());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        return "company_view";
    }
}
