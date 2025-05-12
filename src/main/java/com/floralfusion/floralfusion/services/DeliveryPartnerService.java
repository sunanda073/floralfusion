package com.floralfusion.floralfusion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.floralfusion.floralfusion.entities.Admin;
import com.floralfusion.floralfusion.entities.DeliveryPartner;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.repositories.AdminRepository;
import com.floralfusion.floralfusion.repositories.DeliveryPartnerRepository;
import com.floralfusion.floralfusion.repositories.UserRepository;

@Service
public class DeliveryPartnerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    public void addDeliveryPartner(
        String fname,
        String lname,
        String email,
        String phone,
        String password,
        String country,
        String state,
        String city,
        String area,
        Long adminId
    ) {
        // Check for existing email
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Fetch admin
        Admin admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        // Create new User
        User user = new User();
        user.setFname(fname);
        user.setLname(lname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password); // You can encode it with BCrypt later
        user.setRole("DELIVERY_PARTNER");
        user.setCountry(country);
        user.setState(state);
        user.setCity(city);

        user = userRepository.save(user);

        // Create DeliveryPartner
        DeliveryPartner partner = new DeliveryPartner();
        partner.setUser(user);
        partner.setAdmin(admin);
        partner.setArea(area);
        partner.setRating(0.0);
        partner.setCurrentDeliveries(0);
        partner.setMaxCapacity(5);

        deliveryPartnerRepository.save(partner);
    }

    public List<DeliveryPartner> getDeliveryPartnersByAdminId(Long adminId) {
        return deliveryPartnerRepository.findByAdmin_AdminID(adminId);
    }
}
