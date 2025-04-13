package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.Customer;
import com.floralfusion.floralfusion.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find reviews by productID (use productID from the Product entity)
    List<Review> findByProductProductID(Long productID);

    // Find all reviews by customer
    List<Review> findByCustomer(Customer customer);

    // Find reviews by rating
    List<Review> findByRating(int rating);
}
