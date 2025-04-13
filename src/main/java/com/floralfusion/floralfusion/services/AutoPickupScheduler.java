package com.floralfusion.floralfusion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.enums.FlowerType;

@Service
public class AutoPickupScheduler {

    @Autowired
    private ContributorService contributorService;

    @Autowired
    private PickupService pickupService;

    // Runs every Monday at 9 AM
    @Scheduled(cron = "0 0 9 * * MON", zone = "Asia/Kolkata")
    public void generateWeeklyPickupRequests() {
        System.out.println("Weekly pickup request generation triggered for contributors.!!!!!");

        List<FlowerContributor> contributors = contributorService.getAllWithWeeklyAutoPickup();

        for (FlowerContributor contributor : contributors) {
            double amount = contributor.getDefaultWasteAmountInKg() != null
                    ? contributor.getDefaultWasteAmountInKg()
                    : 1.0;

            String address = contributor.getDefaultPickupAddress();
            if (address == null || address.trim().isEmpty()) {
                continue; // skip if address not set
            }

            FlowerType flowerType = contributor.getDefaultFlowerType();
            if (flowerType == null) {
                continue; // Skip if flower type is not set
            }

            pickupService.createRequest(contributor, amount, address, flowerType);

            int coinsEarned = (int) Math.round(amount);
            contributor.addCoins(coinsEarned);
            contributorService.save(contributor);
        }
    }

}
