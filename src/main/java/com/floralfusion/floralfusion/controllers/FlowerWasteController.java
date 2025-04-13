package com.floralfusion.floralfusion.controllers;

import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.entities.FlowerWaste;
import com.floralfusion.floralfusion.services.ContributorService;
import com.floralfusion.floralfusion.services.FlowerWasteService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class FlowerWasteController {

    private final FlowerWasteService flowerWasteService;

    private final ContributorService contributorService;

    public FlowerWasteController(FlowerWasteService flowerWasteService, ContributorService contributorService) {
        this.flowerWasteService = flowerWasteService;
        this.contributorService = contributorService;
    }

    @GetMapping("/contribute")
    public String showContributionForm(Model model, @RequestParam(value = "success", required = false) String success) {
        model.addAttribute("flowerWaste", new FlowerWaste());
        if (success != null) {
            model.addAttribute("message", "Data added successfully!");
        }
        return "contribution_form";
    }

    @PostMapping("/contribute")
    public String submitContribution(@RequestParam("quantity") int quantity,
            @RequestParam("pickupLocation") String pickupLocation,
            @RequestParam("area") String area,
            @RequestParam("city") String city,
            @RequestParam("pinCode") String pinCode,
            @RequestParam(value = "landmark", required = false) String landmark,
            @RequestParam("pickupDate") LocalDate pickupDate,
            @RequestParam("pickupSlot") String pickupSlot,
            HttpSession session) {

        // Retrieve contributorId from session
        Long contributorId = (Long) session.getAttribute("contributorId");
        if (contributorId == null) {
            throw new RuntimeException("Contributor not logged in");
        }

        flowerWasteService.saveFlowerWaste(contributorId, quantity, pickupLocation, area, city, pinCode, landmark,
                pickupDate, pickupSlot);

        // Fetch contributor's email
        FlowerContributor contributor = contributorService.getContributorById(contributorId);
        String email = contributor.getUser().getEmail();

        // Redirect with email and success flag
        return "redirect:/contributor?email=" + email + "&success=true";
    }
}