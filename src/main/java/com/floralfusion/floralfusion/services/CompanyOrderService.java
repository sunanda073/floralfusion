package com.floralfusion.floralfusion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 

import com.floralfusion.floralfusion.entities.CompanyOrder;
import com.floralfusion.floralfusion.repositories.CompanyOrderRepository;

@Service  // Marking this class as a Spring Service
public class CompanyOrderService {
    

    @Autowired
    private CompanyOrderRepository companyOrderRepository;

    // Fetch orders by company
    public List<CompanyOrder> getAllOrdersForBusiness(Long companyId) {
        // Assuming you have a method to fetch orders for a given company
        return companyOrderRepository.findAllByCompany_CompanyID(companyId);
    }

    public CompanyOrder getDeliveryDetails(Long orderId) {
        return companyOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
    }
}
