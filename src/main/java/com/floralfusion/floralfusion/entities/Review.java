package com.floralfusion.floralfusion.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;

    private String reviewText; // The text content of the review
    private int rating;        // Rating out of 5

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;  // The customer who gave the review

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;    // The product being reviewed

    // No-argument constructor
    public Review() {
    }

    // Constructor with parameters
    public Review(String reviewText, int rating, Customer customer, Product product) {
        this.reviewText = reviewText;
        this.rating = rating;
        this.customer = customer;
        this.product = product;
    }

    // Getters and Setters
    public Long getReviewID() {
        return reviewID;
    }

    public void setReviewID(Long reviewID) {
        this.reviewID = reviewID;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
