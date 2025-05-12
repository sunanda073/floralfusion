package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.CustomerOrder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

    // Find orders by customer ID
    List<CustomerOrder> findByCustomerCustomerID(Long customerID);

    List<CustomerOrder> findByOrderStatus(String orderStatus);

    List<CustomerOrder> findByPaymentStatus(String paymentStatus);
}
