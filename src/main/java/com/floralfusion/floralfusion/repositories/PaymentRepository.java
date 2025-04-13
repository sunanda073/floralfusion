package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.Payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by order ID
    Payment findByOrderOrderID(Long orderId);

    // Find payment by payment status
    List<Payment> findByPaymentStatus(String paymentStatus);

}
