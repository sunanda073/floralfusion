package com.floralfusion.floralfusion.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;

    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    // One-to-many relationship with Order entity (Customer places many orders)
    @OneToMany(mappedBy = "customer")
    private List<CustomerOrder> orderHistory;

    // One-to-many relationship with Review entity (Customer writes many reviews)
    @OneToMany(mappedBy = "customer")
    private List<Review> reviewHistory;

    
    // Getters and Setters
    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CustomerOrder> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<CustomerOrder> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public List<Review> getReviewHistory() {
        return reviewHistory;
    }

    public void setReviewHistory(List<Review> reviewHistory) {
        this.reviewHistory = reviewHistory;
    }
}
