package com.floralfusion.floralfusion.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.floralfusion.floralfusion.repositories.CollectionRequestRepository;

@Service
public class CollectorStatsService {

    @Autowired
    private CollectionRequestRepository requestRepo;

    // public double getTodayCollectedWeight(Long collectorId) {
    //     LocalDate today = LocalDate.now();
    //     return requestRepo.sumFlowerWasteByCollectorAndDate(collectorId, today)
    //                       .orElse(0.0);
    // }

    // public int getTodayCompletedCount(Long collectorId) {
    //     LocalDate today = LocalDate.now();
    //     return requestRepo.countCompletedRequestsByCollectorAndDate(collectorId, today);
    // }

    // public int getPendingRequestCount(Long collectorId) {
    //     return requestRepo.countByCollectorAndStatus(collectorId, RequestStatus.PENDING);
    // }

    // public double getAverageDailyWaste(Long collectorId) {
    //     return requestRepo.calculateAverageWastePerDay(collectorId)
    //                       .orElse(0.0);
    // }
}
