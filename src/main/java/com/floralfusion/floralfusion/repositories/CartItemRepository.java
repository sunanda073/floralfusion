package com.floralfusion.floralfusion.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.floralfusion.floralfusion.entities.CartItem;
import com.floralfusion.floralfusion.entities.Customer;
import com.floralfusion.floralfusion.entities.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(Customer customer);
    Optional<CartItem> findByCustomerAndProduct(Customer customer, Product product);
}

