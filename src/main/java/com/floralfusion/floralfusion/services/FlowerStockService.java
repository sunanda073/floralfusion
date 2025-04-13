package com.floralfusion.floralfusion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.floralfusion.floralfusion.entities.FlowerStock;
import com.floralfusion.floralfusion.repositories.FlowerStockRepository;

@Service
public class FlowerStockService {
    @Autowired
    private FlowerStockRepository flowerStockRepository;

    public int getTotalStock() {
        Integer totalStock = flowerStockRepository.getTotalStockQuantity();
        return (totalStock != null) ? totalStock : 0;
    }

    public List<FlowerStock> getAllFlowerStock() {
        return flowerStockRepository.findAll();
    }
}
