package com.floralfusion.floralfusion.controllers;

import com.floralfusion.floralfusion.entities.CollectionRequest;
import com.floralfusion.floralfusion.entities.Collector;
import com.floralfusion.floralfusion.entities.FlowerStock;
import com.floralfusion.floralfusion.entities.PickupRequest;
import com.floralfusion.floralfusion.enums.FlowerType;
import com.floralfusion.floralfusion.enums.RequestStatus;
import com.floralfusion.floralfusion.repositories.CollectorRepository;
import com.floralfusion.floralfusion.repositories.FlowerStockRepository;
import com.floralfusion.floralfusion.repositories.PickupRepository;
import com.floralfusion.floralfusion.services.CollectionRequestService;
import com.floralfusion.floralfusion.services.CollectorService;
import com.floralfusion.floralfusion.services.PickupService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/collectors")
public class CollectorController {

    @Autowired
    private CollectorService collectorService;

    @Autowired
    private PickupService pickupRequestService;

    @Autowired
    private PickupRepository pickupRequestRepository;

    @Autowired
    private CollectorRepository collectorRepository;

    @Autowired
    private FlowerStockRepository flowerStockRepository;

    @GetMapping("/collector")
    public String showCollectorDashboard(Model model, HttpSession session) {
        Collector collector = (Collector) session.getAttribute("collector");

        if (collector == null) {
            return "redirect:/login?error=unauthorized";
        }

        List<PickupRequest> pickupRequests = pickupRequestRepository
                .findByCollector_CollectorIDAndStatus(collector.getCollectorID(), RequestStatus.PENDING);
        model.addAttribute("pickupRequests", pickupRequests);

        List<PickupRequest> pastCollections = pickupRequestRepository
                .findByCollector_CollectorIDAndStatusIn(collector.getCollectorID(),
                        List.of(RequestStatus.COMPLETED, RequestStatus.CANCELLED));

        model.addAttribute("pastCollections", pastCollections);

        // Fixed service name
        model.addAttribute("collector", collector);
        model.addAttribute("pickupRequests", pickupRequests);

        model.addAttribute("todayWasteCollected", collectorService.getTodayWasteCollected(collector.getCollectorID()));
        model.addAttribute("todayCompletedRequests",
                collectorService.getTodayCompletedRequests(collector.getCollectorID()));
        model.addAttribute("pendingRequestsCount",
                collectorService.getPendingRequestsCount(collector.getCollectorID()));
        model.addAttribute("avgDailyWaste", collectorService.getAverageDailyCollection(collector.getCollectorID()));

        System.out.println("Pickup Requests: " + pickupRequests.size());

        boolean hasCollected = pastCollections.stream()
                .anyMatch(request -> request.getStatus() == RequestStatus.COMPLETED);

        boolean hasCancelled = pastCollections.stream()
                .anyMatch(request -> request.getStatus() == RequestStatus.CANCELLED);

        model.addAttribute("hasCollected", hasCollected);
        model.addAttribute("hasCancelled", hasCancelled);

        return "collector_view";
    }

    @PostMapping("/add")
    public String addCollector(
            @RequestParam String fname,
            @RequestParam String lname,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam String country,
            @RequestParam String state,
            @RequestParam String city,
            @RequestParam String area,
            @RequestParam Long adminId,
            Model model,
            RedirectAttributes redirectAttributes) {

        System.out.println("Controller received Admin ID: " + adminId);

        collectorService.addCollector(fname, lname, email, phone, password, country, state, city, area, adminId);

        // Optionally pass a flash attribute for success message
        redirectAttributes.addFlashAttribute("successMessage", "Collector added successfully!");

        return "redirect:/admin"; // Redirect to the GET /admin mapping
    }

    @GetMapping("/collector/request-details/{id}")
    public String getRequestDetails(@PathVariable Long id, Model model) {
        Optional<PickupRequest> optionalRequest = pickupRequestRepository.findById(id);
        if (optionalRequest.isEmpty())
            return "fragments/empty :: empty";

        model.addAttribute("request", optionalRequest.get());
        return "fragments/request_details :: requestDetails";
    }

    @PostMapping("/collector/mark-collected/{id}")
    public String markAsCollected(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<PickupRequest> optionalRequest = pickupRequestRepository.findById(id);

        if (optionalRequest.isPresent()) {
            PickupRequest request = optionalRequest.get();

            // Update pickup request status
            request.setStatus(RequestStatus.COMPLETED);
            request.setActualPickupTime(LocalDateTime.now());

            // Mark the collector as available
            Collector collector = request.getCollector();
            if (collector != null) {
                collector.setAvailable(1);
                collectorRepository.save(collector);
            }

            // Update flower stock
            FlowerType type = request.getFlowerType();
            int quantityToAdd = (int) Math.round(request.getWasteAmountInKg()); // round or convert as needed

            FlowerStock stock = flowerStockRepository.findById(type).orElse(new FlowerStock());
            stock.setFlowerType(type);
            stock.setQuantity(stock.getQuantity() + quantityToAdd);

            flowerStockRepository.save(stock);


            pickupRequestRepository.save(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Pickup marked as collected and collector marked available!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Pickup request not found.");
        }

        return "redirect:/collectors/collector";
    }

}
