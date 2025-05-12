package com.floralfusion.floralfusion.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.floralfusion.floralfusion.enums.PaymentStatus;
import com.floralfusion.floralfusion.enums.DeliveryStatus;
import com.floralfusion.floralfusion.enums.FlowerType;

@Entity
@Table(name = "company_orders")
public class CompanyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private double quantityInKg;
    private double price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime orderedAt;
    private LocalDateTime paymentCompletedAt;

    @Enumerated(EnumType.STRING)
    private FlowerType flowerType;

    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "delivery_partner_id")
    private DeliveryPartner deliveryPartner;

    private LocalDateTime assignedAt;
    private LocalDateTime expectedDeliveryBy;
    private LocalDateTime deliveredAt;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    // ✅ Bidirectional relationship with Payment (mappedBy)
    @OneToOne(mappedBy = "companyOrder", cascade = CascadeType.ALL)
    private Payment payment;

    // Constructors
    public CompanyOrder() {
        this.orderedAt = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
        this.deliveryStatus = DeliveryStatus.PENDING;
    }

    public CompanyOrder(Company company, double quantityInKg, double price) {
        this();
        this.company = company;
        this.quantityInKg = quantityInKg;
        this.price = price;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public double getQuantityInKg() {
        return quantityInKg;
    }

    public void setQuantityInKg(double quantityInKg) {
        this.quantityInKg = quantityInKg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    public LocalDateTime getPaymentCompletedAt() {
        return paymentCompletedAt;
    }

    public void setPaymentCompletedAt(LocalDateTime paymentCompletedAt) {
        this.paymentCompletedAt = paymentCompletedAt;
    }

    public FlowerType getFlowerType() {
        return flowerType;
    }

    public void setFlowerType(FlowerType flowerType) {
        this.flowerType = flowerType;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getExpectedDeliveryBy() {
        return expectedDeliveryBy;
    }

    public void setExpectedDeliveryBy(LocalDateTime expectedDeliveryBy) {
        this.expectedDeliveryBy = expectedDeliveryBy;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
