package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowerContributorRepository extends JpaRepository<FlowerContributor, Long> {

    // Find by User ID
    FlowerContributor findByUser_UserID(Long userID);

    // Find by User object
    FlowerContributor findByUser(User user);

    // Find by email (via User relation)
    FlowerContributor findByUser_Email(String email);

    @Query("SELECT fc FROM FlowerContributor fc WHERE fc.user.email = :email")
    FlowerContributor findByUserEmail(String email);

    List<FlowerContributor> findByWeeklyAutoPickupTrue();

}
