package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.FlowerWaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FlowerWasteRepository extends JpaRepository<FlowerWaste, Long> {
    
    List<FlowerWaste> findByFlowerContributor_ContributorID(Long contributorID);
    
    List<FlowerWaste> findByCollector_CollectorID(Long collectorID);
}
