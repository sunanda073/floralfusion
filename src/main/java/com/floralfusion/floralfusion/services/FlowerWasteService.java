package com.floralfusion.floralfusion.services;

import com.floralfusion.floralfusion.entities.FlowerWaste;
import com.floralfusion.floralfusion.enums.FlowerType;
import com.floralfusion.floralfusion.enums.WasteStatus;
import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.entities.FlowerStock;
import com.floralfusion.floralfusion.repositories.FlowerWasteRepository;
import com.floralfusion.floralfusion.repositories.FlowerContributorRepository;
import com.floralfusion.floralfusion.repositories.FlowerStockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class FlowerWasteService {

    @Autowired
    private FlowerWasteRepository flowerWasteRepository;

    @Autowired
    private FlowerContributorRepository contributorRepository;

     @Autowired
    private FlowerStockRepository flowerStockRepository; 

    public void saveFlowerWaste(Long contributorId, int quantity, String pickupLocation, String area, String city,
                                String pinCode, String landmark, LocalDate pickupDate, String pickupSlot) {
        
        // Fetch contributor (throws exception if not found)
        FlowerContributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(() -> new RuntimeException("Contributor not found!"));

        // Map pickup slot to time
        LocalTime pickupTime = mapPickupSlotToTime(pickupSlot);

        // Create FlowerWaste object
        FlowerWaste flowerWaste = new FlowerWaste();
        flowerWaste.setQuantity(quantity);
        flowerWaste.setPickupLocation(pickupLocation);
        flowerWaste.setArea(area);
        flowerWaste.setCity(city);
        flowerWaste.setPinCode(pinCode);
        flowerWaste.setLandmark(landmark);
        flowerWaste.setCollectionDate(pickupDate); // Capture current date
        flowerWaste.setPickupTime(pickupTime);
        flowerWaste.setStatus(WasteStatus.PENDING);  // Default status
        flowerWaste.setFlowerContributor(contributor);  // Link to contributor

        // Save to database
        flowerWasteRepository.save(flowerWaste);
    }

    private void updateFlowerStock(FlowerType flowerType, int quantity, FlowerWaste flowerWaste) {
        FlowerStock flowerStock = flowerStockRepository.findById(flowerType)
                .orElse(new FlowerStock(flowerType, 0)); // Create if not exists

        flowerStock.setQuantity(flowerStock.getQuantity() + quantity);
        flowerWaste.setFlowerStock(flowerStock);
        flowerStockRepository.save(flowerStock);
    }

    private LocalTime mapPickupSlotToTime(String pickupSlot) {
        return switch (pickupSlot) {
            case "morning" -> LocalTime.of(9, 0);
            case "afternoon" -> LocalTime.of(13, 0);
            case "evening" -> LocalTime.of(17, 0);
            default -> LocalTime.of(0, 0);
        };
    }
}
