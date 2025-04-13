package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.CollectionRequest;
import com.floralfusion.floralfusion.entities.Collector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
// import com.floralfusion.floralfusion.enums.RequestStatus;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRequestRepository extends JpaRepository<CollectionRequest, Long> {
    List<CollectionRequest> findByCompany_CompanyID(Long companyId);

    public List<CollectionRequest> findByCollector(Collector collector);

//     @Query("SELECT SUM(r.flowerWasteAmount) FROM CollectionRequest r " +
//            "WHERE r.collector.collectorID = :collectorId AND r.updatedAt = :date AND r.status = com.floralfusion.floralfusion.enums.RequestStatus.COLLECTED")
//     Optional<Double> sumFlowerWasteByCollectorAndDate(@Param("collectorId") Long collectorId,
//                                                       @Param("date") LocalDate date);

//     @Query("SELECT COUNT(r) FROM CollectionRequest r " +
//            "WHERE r.collector.collectorID = :collectorId AND r.updatedAt = :date AND r.status = com.floralfusion.floralfusion.enums.RequestStatus.COLLECTED")
//     int countCompletedRequestsByCollectorAndDate(@Param("collectorId") Long collectorId,
//                                                  @Param("date") LocalDate date);

//     @Query("SELECT COUNT(r) FROM CollectionRequest r " +
//            "WHERE r.collector.collectorID = :collectorId AND r.status = com.floralfusion.floralfusion.enums.RequestStatus.PENDING")
//     int countPendingRequests(@Param("collectorId") Long collectorId);

//     @Query("SELECT AVG(r.flowerWasteAmount) FROM CollectionRequest r " +
//            "WHERE r.collector.collectorID = :collectorId AND r.status = com.floralfusion.floralfusion.enums.RequestStatus.COLLECTED")
//     Optional<Double> calculateAverageWastePerDay(@Param("collectorId") Long collectorId);

//     @Query("SELECT COUNT(r) FROM CollectionRequest r " +
//            "WHERE r.collector.collectorID = :collectorId AND r.status = :status")
//     int countByCollectorAndStatus(@Param("collectorId") Long collectorId,
//                                   @Param("status") RequestStatus status);
}

