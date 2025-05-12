package com.floralfusion.floralfusion.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.floralfusion.floralfusion.entities.CompanyOrder;
import com.floralfusion.floralfusion.entities.DeliveryPartner;
import com.floralfusion.floralfusion.enums.DeliveryStatus;
import com.floralfusion.floralfusion.repositories.CompanyOrderRepository;
import com.floralfusion.floralfusion.repositories.DeliveryPartnerRepository;
import com.floralfusion.floralfusion.services.CompanyOrderService;
import com.floralfusion.floralfusion.services.DeliveryPartnerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/delivery-partners")
public class DeliveryPartnerController {

    @Autowired
    private DeliveryPartnerService deliveryPartnerService;

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Autowired
    private CompanyOrderRepository companyOrderRepository;

    @Autowired
    private CompanyOrderService companyOrderService;

    @PostMapping("/add")
    public String addDeliveryPartner(
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
            RedirectAttributes redirectAttributes) {
        try {
            deliveryPartnerService.addDeliveryPartner(
                    fname, lname, email, phone, password,
                    country, state, city, area, adminId);
            redirectAttributes.addFlashAttribute("deliverySuccessMessage", "Delivery Partner added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }

        return "redirect:/admin";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session, @RequestParam(required = false) String success) {
        // 1. Session check
        Long partnerId = (Long) session.getAttribute("deliveryPartnerId");
        if (partnerId == null) {
            return "redirect:/login";
        }

        // 2. Load DeliveryPartner
        DeliveryPartner partner = deliveryPartnerRepository
                .findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid partner ID"));

        // 3. Compute “today” window
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        int todayCount = companyOrderRepository
                .countByDeliveryPartnerAndAssignedAtBetween(
                        partner, startOfDay, endOfDay);
        int completedTodayCount = companyOrderRepository
                .countByDeliveryPartnerAndDeliveredAtBetweenAndDeliveryStatus(
                        partner, startOfDay, endOfDay, DeliveryStatus.DELIVERED);
        int pendingCount = companyOrderRepository
                .countByDeliveryPartnerAndDeliveryStatus(
                        partner, DeliveryStatus.PENDING);

        // 4. Average daily deliveries
        Double avgDaily = companyOrderRepository
                .calculateAverageDailyForPartner(partner.getDeliveryPartnerId());

        // 5. Assigned vs. past lists
        List<CompanyOrder> assignedDeliveries = companyOrderRepository
                .findByDeliveryPartnerAndDeliveryStatus(
                        partner, DeliveryStatus.PENDING);
        List<CompanyOrder> pastDeliveries = companyOrderRepository
                .findByDeliveryPartnerAndDeliveryStatus(
                        partner, DeliveryStatus.DELIVERED);

        // Add success message to the model if present
        if (success != null && success.equals("delivered")) {
            model.addAttribute("success", "Order marked as delivered successfully!");
        }

        // 6. Add to model
        model.addAttribute("deliveryPartner", partner);
        model.addAttribute("todayCount", todayCount);
        model.addAttribute("completedTodayCount", completedTodayCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("avgDaily", avgDaily);
        model.addAttribute("assignedDeliveries", assignedDeliveries);
        model.addAttribute("pastDeliveries", pastDeliveries);

        return "deliverypartner_view";
    }

    @GetMapping("/delivery/request-details/{orderId}")
    public String getDeliveryDetails(@PathVariable("orderId") Long orderId, Model model) {
        // Load delivery details using orderId
        CompanyOrder details = companyOrderService.getDeliveryDetails(orderId);
        model.addAttribute("details", details);
        return "fragments/delivery_details"; // Return a Thymeleaf view
    }

    @PostMapping("/delivery/mark-delivered/{orderId}")
    public String markOrderAsDelivered(@PathVariable Long orderId, Model model) {
        CompanyOrder order = companyOrderRepository.findById(orderId).orElse(null);
        if (order == null || order.getDeliveryPartner() == null) {
            return "redirect:/delivery-partners/dashboard?error=orderNotFound";
        }

        // Update delivery status and timestamp
        order.setDeliveredAt(LocalDateTime.now());
        order.setDeliveryStatus(DeliveryStatus.DELIVERED);
        companyOrderRepository.save(order);

        // Reduce current deliveries of the delivery partner
        DeliveryPartner partner = order.getDeliveryPartner();
        if (partner.getCurrentDeliveries() > 0) {
            partner.setCurrentDeliveries(partner.getCurrentDeliveries() - 1);
            deliveryPartnerRepository.save(partner);
        }

        // Redirect to the dashboard after successful operation
        return "redirect:/delivery-partners/dashboard?success=delivered";
    }
}
