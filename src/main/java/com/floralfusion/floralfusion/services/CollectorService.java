package com.floralfusion.floralfusion.services;

import com.floralfusion.floralfusion.entities.Admin;
import com.floralfusion.floralfusion.entities.Collector;
import com.floralfusion.floralfusion.entities.PickupRequest;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.repositories.AdminRepository;
import com.floralfusion.floralfusion.repositories.CollectorRepository;
import com.floralfusion.floralfusion.repositories.PickupRepository;
import com.floralfusion.floralfusion.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CollectorService {

    @Autowired
    private CollectorRepository collectorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PickupRepository pickupRequestRepository;

    public List<Collector> getAllCollectors() {
        return collectorRepository.findAll(); // Fetching all collectors from the database
    }

    public String addCollector(String fname, String lname, String email, String phone, String password,
            String country, String state, String city, String area, Long adminId) {

        System.out.println("Incoming Admin ID: " + adminId);

        // Create a User object
        User user = new User();
        user.setFname(fname);
        user.setLname(lname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setCountry(country);
        user.setState(state);
        user.setCity(city);
        user.setRole("Collector");

        // Save User to Database
        User savedUser = userRepository.save(user);

        // Fetch Admin by adminId

        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));

        // Create Collector object and link to user & admin
        Collector collector = new Collector();
        collector.setUser(savedUser);
        collector.setAdmin(admin);
        collector.setArea(area);
        collector.setRating(0.0);

        // Save Collector to Database
        collectorRepository.save(collector);

        return "Collector added successfully!";
    }

    public int countCollectorsByAdmin(Long adminId) {
        return collectorRepository.countByAdmin_AdminID(adminId);
    }

    public List<Collector> getTopCollectors() {
        return collectorRepository.findTopCollectorsToday(PageRequest.of(0, 5));
    }

    public double getTodayWasteCollected(Long collectorId) {
        LocalDate today = LocalDate.now();
        return pickupRequestRepository.sumWasteCollectedToday(collectorId, today);
    }

    public long getTodayCompletedRequests(Long collectorId) {
        LocalDate today = LocalDate.now();
        return pickupRequestRepository.countCompletedToday(collectorId, today);
    }

    public long getPendingRequestsCount(Long collectorId) {
        return pickupRequestRepository.countPendingByCollector(collectorId);
    }

    public double getAverageDailyCollection(Long collectorId) {
        return pickupRequestRepository.calculateAvgDailyCollection(collectorId);
    }

    public void saveRating(PickupRequest pickupRequest, int rating) {
        // Save the rating in the PickupRequest entity
        pickupRequest.setRating(rating);
        pickupRequestRepository.save(pickupRequest); 

        // Get the collector from the pickup request
        Collector collector = pickupRequest.getCollector();

        // Calculate the new rating (simple average, you can adjust as needed)
        double currentRating = collector.getRating();
        int totalRatings = pickupRequestRepository.countByCollectorAndRatingIsNotNull(collector); // Only count rated pickups

        // Update the rating - this can be more sophisticated depending on how you want
        // to calculate the average
        double newRating = (currentRating * totalRatings + rating) / (totalRatings + 1);

        // Update the collector's rating
        collector.setRating(newRating);

        // Save the updated collector to the database
        collectorRepository.save(collector);
    }

}
