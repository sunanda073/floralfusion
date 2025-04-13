package com.floralfusion.floralfusion.services;

import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.entities.FlowerWaste;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.repositories.FlowerWasteRepository;
import com.floralfusion.floralfusion.repositories.FlowerContributorRepository;
import com.floralfusion.floralfusion.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributorService {

    private final UserRepository userRepository;
    private final FlowerWasteRepository flowerWasteRepository;
    private final FlowerContributorRepository flowerContributorRepository;

    public ContributorService(UserRepository userRepository, 
                              FlowerWasteRepository flowerWasteRepository, 
                              FlowerContributorRepository flowerContributorRepository) {
        this.userRepository = userRepository;
        this.flowerWasteRepository = flowerWasteRepository;
        this.flowerContributorRepository = flowerContributorRepository;
    }

    public FlowerContributor getContributorById(Long contributorId) {
        return flowerContributorRepository.findById(contributorId)
                .orElseThrow(() -> new RuntimeException("Contributor not found with ID: " + contributorId));
    }

    // Fetch FlowerContributor by email
    public FlowerContributor getContributorByEmail(String email) {
        return flowerContributorRepository.findByUser_Email(email);
    }

    // Fetch user by email and password
    public User getContributorByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).orElse(null);
    }

    // Fetch contributions by contributor ID
    public List<FlowerWaste> getContributorContributions(Long contributorId) {
        return flowerWasteRepository.findByFlowerContributor_ContributorID(contributorId);
    }

    public FlowerContributor getByEmail(String email) {
        return flowerContributorRepository.findByUserEmail(email);
    }

    public FlowerContributor save(FlowerContributor contributor) {
        return flowerContributorRepository.save(contributor);
    }

    public List<FlowerContributor> getAllWithWeeklyAutoPickup() {
        return flowerContributorRepository.findByWeeklyAutoPickupTrue();
    }
    
}

