package com.floralfusion.floralfusion.services;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.floralfusion.floralfusion.entities.Collector;
import com.floralfusion.floralfusion.entities.PickupRequest;
import com.floralfusion.floralfusion.repositories.CollectorRepository;
import com.floralfusion.floralfusion.repositories.PickupRepository;

@Component
public class PickupRequestProcessor {

    @Autowired
    private PickupRequestQueue pickupRequestQueue;

    @Autowired
    private PickupRepository pickupRequestRepository;

    @Autowired
    private CollectorRepository collectorRepository;

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void processQueuedRequests() {
        while (!pickupRequestQueue.isEmpty()) {
            PickupRequest request = pickupRequestQueue.takeRequest();
            Collector collector = assignCollector(request.getContributor().getArea());
            if (collector != null) {
                request.setCollector(collector);
                pickupRequestRepository.save(request);
                // Optionally notify the contributor about the assignment
            } else {
                // Re-add the request to the queue for future processing
                pickupRequestQueue.addRequest(request);
                break; // Exit the loop to prevent continuous reprocessing
            }
        }
    }

    private Collector assignCollector(String contributorArea) {
        List<Collector> availableCollectors = collectorRepository.findByAreaAndIsAvailable(contributorArea, 1);
        return availableCollectors.stream()
                .max(Comparator.comparingDouble(Collector::getRating))
                .orElse(null);
    }
}

