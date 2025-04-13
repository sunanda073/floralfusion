package com.floralfusion.floralfusion.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.floralfusion.floralfusion.entities.CollectionRequest;
import com.floralfusion.floralfusion.entities.Company;
import com.floralfusion.floralfusion.services.CollectionRequestService;
import com.floralfusion.floralfusion.services.CompanyService;

import jakarta.servlet.http.HttpSession;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class CollectionController {

    private final CompanyService companyService;
    private final CollectionRequestService collectionService;

    public CollectionController(CollectionRequestService collectionService, CompanyService companyService) {
        this.collectionService = collectionService;
        this.companyService = companyService;
    }

    // @PostMapping("/submit-collection")
    // public String createWeeklyCollection(@RequestParam("weeklyAmount") double weeklyAmount,
    //         @RequestParam("collectionDay") String dayOfWeek,
    //         @RequestParam("collectionTime") String preferredTime,
    //         Model model) {

    //     try {
    //         LocalTime collectionTime = LocalTime.parse(preferredTime);
    //         LocalDate nextCollectionDate = getNextCollectionDate(dayOfWeek);

    //         // Assuming there is a method to fetch the logged-in user's company
    //         Company company = companyService.getLoggedInUserCompany()
    //                 .orElseThrow(() -> new RuntimeException("Company not found"));

    //         // Create and populate the CollectionRequest object
    //         CollectionRequest request = new CollectionRequest();
    //         request.setCompany(company);
    //         request.setCollectionType(CollectionRequest.CollectionType.WEEKLY);
    //         request.setFlowerWasteAmount(weeklyAmount);
    //         request.setCollectionDay(dayOfWeek);
    //         request.setCollectionTime(collectionTime);
    //         request.setUrgentDate(nextCollectionDate); // For next collection
    //         request.setStatus(CollectionRequest.RequestStatus.PENDING);

    //         // // Save the request
    //         // collectionService.createWeeklyCollection(company, weeklyAmount, dayOfWeek, collectionTime);

    //         model.addAttribute("message", "Weekly collection request submitted successfully!");
    //     } catch (Exception e) {
    //         model.addAttribute("error", "Error submitting collection request: " + e.getMessage());
    //     }

    //     return "scheduleConfirmation";
    // }

    // Helper method to find the next collection date
    private LocalDate getNextCollectionDate(String dayOfWeek) {
        DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        LocalDate today = LocalDate.now();
        int daysUntilNext = (targetDay.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
        return today.plusDays(daysUntilNext == 0 ? 7 : daysUntilNext);
    }

}
