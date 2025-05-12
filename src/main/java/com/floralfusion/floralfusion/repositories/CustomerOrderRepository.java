package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.Customer;
import com.floralfusion.floralfusion.entities.CustomerOrder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    // You can define custom queries here if needed, for example:
    // List<CustomerOrder> findByCustomerId(Long customerId);
    // Optional<CustomerOrder> findByOrderID(Long orderID);

        List<CustomerOrder> findByCustomer(Customer customer);  
    
    

}
