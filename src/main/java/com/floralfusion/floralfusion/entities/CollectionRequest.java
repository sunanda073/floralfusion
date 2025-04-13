package com.floralfusion.floralfusion.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import com.floralfusion.floralfusion.enums.CollectionType;
import com.floralfusion.floralfusion.enums.RequestStatus;

@Entity
public class CollectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false) // Foreign key mapping
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollectionType collectionType; // WEEKLY or URGENT

    @Column(nullable = false)
    private double flowerWasteAmount; // Amount in kg

    private String collectionDay; // For weekly requests (e.g., Monday)

    private LocalTime collectionTime; // Preferred collection time

    private LocalDate urgentDate; // For urgent requests (optional)

    private LocalTime urgentTime; // For urgent requests (optional)

    private String urgentReason; // Reason for urgent request (optional)

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // PENDING, APPROVED, REJECTED, ACTIVE

    private LocalDate createdAt; // Request creation date

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collector collector;

    // Constructors
    public CollectionRequest() {
        this.status = RequestStatus.PENDING;
    }

    // Getters and Setters
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public double getFlowerWasteAmount() {
        return flowerWasteAmount;
    }

    public void setFlowerWasteAmount(double flowerWasteAmount) {
        this.flowerWasteAmount = flowerWasteAmount;
    }

    public String getCollectionDay() {
        return collectionDay;
    }

    public void setCollectionDay(String collectionDay) {
        this.collectionDay = collectionDay;
    }

    public LocalTime getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(LocalTime collectionTime) {
        this.collectionTime = collectionTime;
    }

    public LocalDate getUrgentDate() {
        return urgentDate;
    }

    public void setUrgentDate(LocalDate urgentDate) {
        this.urgentDate = urgentDate;
    }

    public LocalTime getUrgentTime() {
        return urgentTime;
    }

    public void setUrgentTime(LocalTime urgentTime) {
        this.urgentTime = urgentTime;
    }

    public String getUrgentReason() {
        return urgentReason;
    }

    public void setUrgentReason(String urgentReason) {
        this.urgentReason = urgentReason;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

}
