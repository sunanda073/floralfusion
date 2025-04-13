package com.floralfusion.floralfusion.services;

import com.floralfusion.floralfusion.entities.*;
import com.floralfusion.floralfusion.enums.FlowerType;
import com.floralfusion.floralfusion.enums.RequestStatus;
import com.floralfusion.floralfusion.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PickupService {

    @Autowired
    private PickupRepository pickupRequestRepository;

    @Autowired
    private CollectorRepository collectorRepository;

    @Autowired
    private FlowerContributorRepository contributorRepository;

    @Autowired
    private PickupRequestQueue pickupRequestQueue;

    /**
     * Creates a pickup request and assigns a collector if available.
     */
    public void createRequest(FlowerContributor contributor, double wasteAmount, String pickupAddress,
            FlowerType flowerType) {
        PickupRequest request = new PickupRequest();
        request.setContributor(contributor);
        request.setWasteAmountInKg(wasteAmount);
        request.setRequestTime(LocalDateTime.now());
        request.setExpectedPickupTime(LocalDateTime.now().plusHours(24));
        request.setStatus(RequestStatus.PENDING);
        request.setCancelled(false);
        request.setPickupAddress(pickupAddress);
        request.setFlowerType(flowerType); // Ensure this setter exists in PickupRequest

        Collector collector = assignCollector(contributor.getArea());
        if (collector != null) {
            request.setCollector(collector);

            // Set the collector's availability to false
            collector.setAvailable(0);
            collectorRepository.save(collector);

            pickupRequestRepository.save(request);
        } else {
            pickupRequestQueue.addRequest(request);
            // Optionally notify the contributor about the delay
        }

        int coinsEarned = (int) Math.round(wasteAmount);
        request.setCoinsEarned(coinsEarned);
        contributor.addCoins(coinsEarned);

        pickupRequestRepository.save(request);

        contributorRepository.save(contributor);
    }

    /**
     * Assigns the most suitable available collector in the same area.
     */
    private Collector assignCollector(String contributorArea) {
        List<Collector> availableCollectors = collectorRepository.findByAreaAndIsAvailable(contributorArea, 1);

        if (availableCollectors.isEmpty())
            return null;

        return availableCollectors.stream()
                .sorted(Comparator.comparingDouble(Collector::getRating).reversed())
                .findFirst()
                .orElse(null);
    }

    public boolean cancelPickupRequest(Long pickupId, String reason) {
        Optional<PickupRequest> optionalRequest = pickupRequestRepository.findById(pickupId);
    
        if (optionalRequest.isPresent()) {
            PickupRequest pickup = optionalRequest.get();
    
            if (pickup.getStatus() == RequestStatus.PENDING) {
                pickup.setStatus(RequestStatus.CANCELLED);
                pickup.setCancelled(true);
                pickup.setCancellationReason(reason);
                pickup.setActualPickupTime(null);
                pickup.setCancelledAt(LocalDateTime.now()); 
    
                // Free up the collector if one was assigned
                Collector assignedCollector = pickup.getCollector();
                if (assignedCollector != null) {
                    assignedCollector.setAvailable(1);  // Set isAvailable = 1
                    collectorRepository.save(assignedCollector);
                }
    
                pickupRequestRepository.save(pickup);
                return true;
            }
        }
    
        return false;
    }
    

    public List<PickupRequest> findAllByContributorOrderByRequestTimeDesc(FlowerContributor contributor) {
        return pickupRequestRepository.findAllByContributorOrderByRequestTimeDesc(contributor);
    }

    public List<PickupRequest> findByContributor(FlowerContributor contributor) {
        return pickupRequestRepository.findByContributorOrderByRequestTimeDesc(contributor);
    }

    public List<PickupRequest> findByContributorAndStatus(FlowerContributor contributor, String status) {
        RequestStatus requestStatus = RequestStatus.valueOf(status); // converts "PENDING" to enum
        return pickupRequestRepository.findByContributorAndStatusOrderByRequestTimeDesc(contributor, requestStatus);
    }

    public List<PickupRequest> findByCollector(Collector collector) {
        return pickupRequestRepository.findByCollector(collector);
    }

    public PickupRequest getPickupRequestById(Long id) {
        // Return the pickup request if found, or return null if not found
        return pickupRequestRepository.findById(id).orElse(null);
    }

}

