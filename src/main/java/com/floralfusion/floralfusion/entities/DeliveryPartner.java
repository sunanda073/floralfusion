package com.floralfusion.floralfusion.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_partners")
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryPartnerId;  

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)  
    private User user;

    @ManyToOne
    @JoinColumn(name = "admin_id")  
    private Admin admin;

    private String area;
    private double rating;

    @Column(nullable = false)
    private int currentDeliveries = 0;  

    @Column(nullable = false)
    private int maxCapacity = 5;  

    // Getters and Setters

    public Long getDeliveryPartnerId() {  
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(Long deliveryPartnerId) {  
        this.deliveryPartnerId = deliveryPartnerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCurrentDeliveries() {
        return currentDeliveries;
    }

    public void setCurrentDeliveries(int currentDeliveries) {
        this.currentDeliveries = currentDeliveries;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    // Utility method to check if a partner is available for new deliveries
    public boolean isAvailableForNewDelivery() {
        return currentDeliveries < maxCapacity;
    }
}
