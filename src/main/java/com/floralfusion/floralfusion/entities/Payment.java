package com.floralfusion.floralfusion.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;

    private String paymentMethod;   // Payment method (e.g., "Credit Card", "PayPal", etc.)
    private double amount;          // Total amount paid
    private String paymentDate;     // Date of payment
    private String paymentStatus;   // Payment status (e.g., "Paid", "Pending", "Failed")

    @OneToOne
    @JoinColumn(name = "orderID")
    private Order order;  // One-to-One relationship with Order

    // Constructor with parameters
    public Payment(String paymentMethod, double amount, String paymentDate, String paymentStatus, Order order) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.order = order;
    }

    // Default constructor (no-arg constructor)
    public Payment() {
    }

    // Getters and Setters
    public Long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Long paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
