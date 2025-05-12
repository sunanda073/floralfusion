package com.floralfusion.floralfusion.entities;

import java.time.LocalDateTime;

import com.floralfusion.floralfusion.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private String paymentMethod; // Payment method (e.g., "Credit Card", "PayPal", etc.)
    private double amount; // Total amount paid

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime paymentDate; // Using LocalDateTime for better date handling

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // Payment status (e.g., "Paid", "Pending", "Failed")

    @OneToOne
    @JoinColumn(name = "customer_order_id") // <-- Add this
    private CustomerOrder customerOrder;

    @OneToOne
    @JoinColumn(name = "company_order_id", referencedColumnName = "orderID", nullable = true)
    private CompanyOrder companyOrder;

    private String razorpayPaymentId; // Razorpay Payment ID

    // Constructor with parameters
    public Payment(String paymentMethod, double amount, LocalDateTime paymentDate, PaymentStatus paymentStatus,
            CustomerOrder customerOrder, CompanyOrder companyOrder, String razorpayPaymentId) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.customerOrder = customerOrder;
        this.companyOrder = companyOrder;
        this.razorpayPaymentId = razorpayPaymentId;
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

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public CompanyOrder getCompanyOrder() {
        return companyOrder;
    }

    public void setCompanyOrder(CompanyOrder companyOrder) {
        this.companyOrder = companyOrder;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }
}
