package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders by customer ID
    List<Order> findByCustomerCustomerID(Long customerID);

    List<Order> findByOrderStatus(String orderStatus);

    List<Order> findByPaymentStatus(String paymentStatus);
}
