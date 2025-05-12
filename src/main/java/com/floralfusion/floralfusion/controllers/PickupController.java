package com.floralfusion.floralfusion.controllers;

import com.floralfusion.floralfusion.entities.CollectionRequest;
import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.entities.PickupRequest;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.enums.FlowerType;
import com.floralfusion.floralfusion.repositories.FlowerContributorRepository;
import com.floralfusion.floralfusion.services.CollectorService;
import com.floralfusion.floralfusion.services.ContributorService;
import com.floralfusion.floralfusion.services.PickupService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/contributor")
public class PickupController {

    @Autowired
    private ContributorService contributorService;

    @Autowired
    private PickupService pickupService;

    @Autowired
    private FlowerContributorRepository flowerContributorRepository;

    @Autowired
    private CollectorService collectorService;

    @PostMapping("/pickup-request")
    public String createPickupRequest(@RequestParam("waste-amount") double wasteAmount,
            @RequestParam("flowerType") String flowerType,
            @RequestParam(value = "pickup-address", required = false) String pickupAddress,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "error"; 
        }

        FlowerContributor contributor = flowerContributorRepository.findByUser(user);


        if (contributor == null) {
            model.addAttribute("error", "Contributor not found.");
            return "error"; 
        }

        FlowerType type;
        try {
            type = FlowerType.valueOf(flowerType.toUpperCase()); 
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid flower type specified.");
            return "error"; 
        }


        pickupService.createRequest(contributor, wasteAmount, pickupAddress, type);

        return "redirect:/contributor";
    }


    @PostMapping("/toggle-weekly-pickup")
    public String toggleWeeklyAutoPickup(
            @RequestParam(value = "weeklyAutoPickup", required = false) String autoPickup,
            @RequestParam("flowerType") FlowerType flowerType,
            @RequestParam(value = "defaultWasteAmountInKg", required = false) Double defaultWasteAmount,
            @RequestParam(value = "defaultPickupAddress", required = false) String defaultPickupAddress,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "error";
        }

        FlowerContributor contributor = flowerContributorRepository.findByUser(user);

        if (contributor == null) {
            model.addAttribute("error", "Contributor not found.");
            return "error";
        }

        boolean enableAutoPickup = autoPickup != null;

        contributor.setWeeklyAutoPickup(enableAutoPickup);
        contributor.setDefaultWasteAmountInKg(defaultWasteAmount);
        contributor.setDefaultPickupAddress(defaultPickupAddress);
        contributor.setDefaultFlowerType(flowerType);
        contributorService.save(contributor);

        return "redirect:/contributor";
    }

    @PostMapping("/stop-weekly-pickup")
    public String stopWeeklyAutoPickup(HttpSession session, Model model) {
        
        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "error"; 
        }


        FlowerContributor contributor = flowerContributorRepository.findByUser(user);

        if (contributor == null) {
            model.addAttribute("error", "Contributor not found.");
            return "error"; 
        }

        contributor.setWeeklyAutoPickup(false);

        contributorService.save(contributor);

        return "redirect:/contributor";
    }

    @GetMapping("/dashboard")
    public String showContributorDashboard(@RequestParam(value = "status", required = false) String status,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "error"; 
        }
        FlowerContributor contributor = flowerContributorRepository.findByUser(user);

        if (contributor == null) {
            return "redirect:/login";
        }

        List<PickupRequest> pickupRequests;
        if (status != null && !status.isEmpty()) {
            pickupRequests = pickupService.findByContributorAndStatus(contributor, status);
        } else {
            pickupRequests = pickupService.findByContributor(contributor);
        }

        model.addAttribute("contributor", contributor);
        model.addAttribute("pickupRequests", pickupRequests);
        model.addAttribute("selectedStatus", status);

        return "flowercontributor_view"; 
    }

    @PostMapping("/rate-collector")
    public String rateCollector(@RequestParam("pickupId") Long pickupId, @RequestParam("rating") int rating) {
       
        PickupRequest pickupRequest = pickupService.getPickupRequestById(pickupId);


        collectorService.saveRating(pickupRequest, rating);

        return "redirect:/contributor";
    }

    @PostMapping("/cancel/{pickupId}")
    public String cancelPickup(@PathVariable Long pickupId,
                                @RequestParam("reason") String reason,
                                RedirectAttributes redirectAttributes) {

        boolean cancelled = pickupService.cancelPickupRequest(pickupId, reason);

        if (cancelled) {
            redirectAttributes.addFlashAttribute("success", "Pickup request cancelled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unable to cancel the pickup request.");
        }

        return "redirect:/contributor";
    }

}
