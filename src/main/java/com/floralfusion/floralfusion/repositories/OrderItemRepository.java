package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.OrderItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Find OrderItems by Order ID
    List<OrderItem> findByOrderOrderID(Long orderId);

    // Find OrderItems by Product's ID
    List<OrderItem> findByProductProductID(Long productId);
}
