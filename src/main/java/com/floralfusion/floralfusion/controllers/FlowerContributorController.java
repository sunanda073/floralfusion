package com.floralfusion.floralfusion.controllers;

import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.entities.FlowerWaste;
import com.floralfusion.floralfusion.entities.PickupRequest;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.services.ContributorService;
import com.floralfusion.floralfusion.services.PickupService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FlowerContributorController {

    private final ContributorService contributorService;

    private final PickupService pickupRequestService;

    public FlowerContributorController(ContributorService contributorService, PickupService pickupRequestService) {
        this.contributorService = contributorService;
        this.pickupRequestService = pickupRequestService;
    }

    @GetMapping("/contributor")
    public String showContributorPage(Model model,
            HttpSession session,
            @RequestParam(value = "success", required = false) String success) {

        if ("true".equals(success)) {
            model.addAttribute("message", "Your Contribution request has been received. Thank you!");
        }

        // Get contributor user from session
        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "error";
        }

        // Fetch contributor details
        FlowerContributor contributor = contributorService.getContributorByEmail(user.getEmail());

        if (contributor == null) {
            model.addAttribute("error", "Contributor not found.");
            return "error";
        }

        List<PickupRequest> pickupRequests = pickupRequestService.findAllByContributorOrderByRequestTimeDesc(contributor);
        model.addAttribute("pickupRequests", pickupRequests);

        model.addAttribute("contributor", contributor);
        model.addAttribute("user", contributor.getUser());

        return "flowercontributor_view"; 
    }

}
