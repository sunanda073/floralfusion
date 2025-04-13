package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.FlowerStock;
import com.floralfusion.floralfusion.enums.FlowerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlowerStockRepository extends JpaRepository<FlowerStock, FlowerType> {
    @Query("SELECT SUM(fs.quantity) FROM FlowerStock fs")
    Integer getTotalStockQuantity();
}

