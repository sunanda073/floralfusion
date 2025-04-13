package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.PickupRequest;
import com.floralfusion.floralfusion.entities.CollectionRequest;
import com.floralfusion.floralfusion.entities.Collector;
import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PickupRepository extends JpaRepository<PickupRequest, Long> {

    // Find all requests by contributor
    List<PickupRequest> findByContributor(FlowerContributor contributor);

    // Find all pending requests for a contributor
    List<PickupRequest> findByContributorAndStatus(FlowerContributor contributor, RequestStatus status);

    // Optional: find by status
    List<PickupRequest> findByStatus(RequestStatus status);

    List<PickupRequest> findAllByContributorOrderByRequestTimeDesc(FlowerContributor contributor);

    List<PickupRequest> findByContributorOrderByRequestTimeDesc(FlowerContributor contributor);

    List<PickupRequest> findByContributorAndStatusOrderByRequestTimeDesc(FlowerContributor contributor,
            RequestStatus status);

    List<PickupRequest> findByCollector(Collector collector);

    List<PickupRequest> findByCollector_CollectorIDAndStatus(Long collectorID, RequestStatus status);

    List<PickupRequest> findByCollector_CollectorIDAndStatusIn(Long collectorID, List<RequestStatus> statuses);

    @Query("SELECT COALESCE(SUM(p.wasteAmountInKg), 0) FROM PickupRequest p " +
            "WHERE p.collector.collectorID = :collectorId AND p.status = 'COMPLETED' " +
            "AND DATE(p.actualPickupTime) = :today")
    double sumWasteCollectedToday(@Param("collectorId") Long collectorId,
            @Param("today") LocalDate today);

    @Query("SELECT COUNT(p) FROM PickupRequest p " +
            "WHERE p.collector.collectorID = :collectorId AND p.status = 'COMPLETED' " +
            "AND DATE(p.actualPickupTime) = :today")
    long countCompletedToday(@Param("collectorId") Long collectorId,
            @Param("today") LocalDate today);

    @Query("SELECT COUNT(p) FROM PickupRequest p " +
            "WHERE p.collector.collectorID = :collectorId AND p.status = 'PENDING'")
    long countPendingByCollector(@Param("collectorId") Long collectorId);

    @Query("SELECT COALESCE(SUM(p.wasteAmountInKg) / COUNT(DISTINCT DATE(p.actualPickupTime)), 0) " +
            "FROM PickupRequest p " +
            "WHERE p.collector.collectorID = :collectorId AND p.status = 'COMPLETED'")
    double calculateAvgDailyCollection(@Param("collectorId") Long collectorId);

    int countByCollectorAndRatingIsNotNull(Collector collector);


}
