package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.Collector;
import com.floralfusion.floralfusion.entities.User;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectorRepository extends JpaRepository<Collector, Long> {

    List<Collector> findByArea(String area);

    List<Collector> findByRatingGreaterThan(double rating);

    Collector findByUser_UserID(Long userID);

    int countByAdmin_AdminID(Long adminID);

    Collector findByUser(User user);

    @Query("""
            SELECT fw.collector AS collector, SUM(fw.quantity) AS totalCollected
            FROM FlowerWaste fw
            WHERE fw.collectionDate = CURRENT_DATE
            AND fw.collector IS NOT NULL
            GROUP BY fw.collector
            ORDER BY totalCollected DESC
            """)
    List<Collector> findTopCollectorsToday(PageRequest pageable);


    List<Collector> findByAreaAndIsAvailable(String area, int isAvailable);



}
