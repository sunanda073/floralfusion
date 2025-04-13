package com.floralfusion.floralfusion.services;

import com.floralfusion.floralfusion.entities.CollectionRequest;
import com.floralfusion.floralfusion.entities.Collector;
import com.floralfusion.floralfusion.entities.Company;
import com.floralfusion.floralfusion.repositories.CollectionRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CollectionRequestService {

    private final CollectionRequestRepository collectionRequestRepository;

    public CollectionRequestService(CollectionRequestRepository collectionRequestRepository) {
        this.collectionRequestRepository = collectionRequestRepository;
    }

    // public void createWeeklyCollection(Company company, double amount, String collectionDay, LocalTime collectionTime) {
    //     CollectionRequest request = new CollectionRequest();
    //     request.setCompany(company);
    //     request.setFlowerWasteAmount(amount);
    //     request.setCollectionType(CollectionRequest.CollectionType.WEEKLY);
    //     request.setCollectionDay(collectionDay);
    //     request.setCollectionTime(collectionTime);
    //     request.setStatus(CollectionRequest.RequestStatus.PENDING);
    //     request.setCreatedAt(LocalDate.now());

    //     collectionRequestRepository.save(request);
    // }

    // @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    // public void generateWeeklyRequests() {
    //     collectionRequestRepository.findAllByCollectionType(CollectionRequest.CollectionType.WEEKLY)
    //             .forEach(this::createNextWeeklyRequest);
    // }

    // // private void createNextWeeklyRequest(CollectionRequest request) {
    // //     DayOfWeek targetDay = DayOfWeek.valueOf(request.getCollectionDay().toUpperCase());
    // //     LocalDate nextCollectionDate = getNextCollectionDate(targetDay);

    // //     CollectionRequest newRequest = new CollectionRequest();
    // //     newRequest.setCompany(request.getCompany());
    // //     newRequest.setFlowerWasteAmount(request.getFlowerWasteAmount());
    // //     newRequest.setCollectionType(CollectionRequest.CollectionType.WEEKLY);
    // //     newRequest.setCollectionDay(request.getCollectionDay());
    // //     newRequest.setCollectionTime(request.getCollectionTime());
    // //     newRequest.setStatus(CollectionRequest.RequestStatus.PENDING);
    // //     newRequest.setCreatedAt(nextCollectionDate);

    // //     collectionRequestRepository.save(newRequest);
    // // }

    // private LocalDate getNextCollectionDate(DayOfWeek targetDay) {
    //     LocalDate today = LocalDate.now();
    //     int daysUntilNext = (targetDay.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
    //     return daysUntilNext == 0 ? today.plusWeeks(1) : today.plusDays(daysUntilNext);
    // }

    public List<CollectionRequest> findByCollector(Collector collector) {
        return collectionRequestRepository.findByCollector(collector);
    }

    // public Double getTodayCollectedWeight(Long collectorId) {
    //     LocalDate today = LocalDate.now();
    //     Optional<Double> result = collectionRequestRepository
    //             .sumFlowerWasteByCollectorAndDate(collectorId, today);
    //     return result.orElse(0.0);
    // }

    // public int getTodayCompletedCount(Long collectorId) {
    //     LocalDate today = LocalDate.now();
    //     return collectionRequestRepository.countCompletedRequestsByCollectorAndDate(collectorId, today);
    // }

    // public int getPendingRequestCount(Long collectorId) {
    //     return collectionRequestRepository.countByCollectorAndStatus(collectorId, RequestStatus.PENDING);
    // }

    // public Double getAverageDailyWaste(Long collectorId) {
    //     Optional<Double> result = collectionRequestRepository.calculateAverageWastePerDay(collectorId);
    //     return result.orElse(0.0);
    // }

}
