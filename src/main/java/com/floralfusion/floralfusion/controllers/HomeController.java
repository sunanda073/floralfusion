package com.floralfusion.floralfusion.controllers;

//import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.floralfusion.floralfusion.entities.CollectionRequest;

import jakarta.servlet.http.HttpSession;

//import com.floralfusion.floralfusion.entities.Product;
//import com.floralfusion.floralfusion.services.ProductService;

@Controller
public class HomeController {

    // @Autowired
    // private ProductService productService;

    @GetMapping("/") // http://localhost:8090
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "success", required = false) String success, Model model) {
        if (success != null && !success.isEmpty()) {
            model.addAttribute("message", success);
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/schedule-collection")
    public String showCollectionForm(Model model) {
        model.addAttribute("collectionRequest", new CollectionRequest());
        return "schedule_view";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");
        return "redirect:/login";
    }

}
