package com.floralfusion.floralfusion.entities;

import com.floralfusion.floralfusion.enums.FlowerType;
import com.floralfusion.floralfusion.enums.RequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class PickupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contributor_id")
    private FlowerContributor contributor;

    private LocalDateTime requestTime;

    private double wasteAmountInKg;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime expectedPickupTime;

    private LocalDateTime actualPickupTime;

    private int coinsEarned;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlowerType flowerType;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collector collector;

    private boolean cancelled;

    private String cancellationReason;

    private LocalDateTime cancelledAt;

    @Column(nullable = true)
    private String pickupAddress;

    @Column(nullable = true)
    private Integer rating;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlowerContributor getContributor() {
        return contributor;
    }

    public void setContributor(FlowerContributor contributor) {
        this.contributor = contributor;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public double getWasteAmountInKg() {
        return wasteAmountInKg;
    }

    public void setWasteAmountInKg(double wasteAmountInKg) {
        this.wasteAmountInKg = wasteAmountInKg;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpectedPickupTime() {
        return expectedPickupTime;
    }

    public void setExpectedPickupTime(LocalDateTime expectedPickupTime) {
        this.expectedPickupTime = expectedPickupTime;
    }

    public LocalDateTime getActualPickupTime() {
        return actualPickupTime;
    }

    public void setActualPickupTime(LocalDateTime actualPickupTime) {
        this.actualPickupTime = actualPickupTime;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public void setCoinsEarned(int coinsEarned) {
        this.coinsEarned = coinsEarned;
    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }
    
    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public FlowerType getFlowerType() {
        return flowerType;
    }

    public void setFlowerType(FlowerType flowerType) {
        this.flowerType = flowerType;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
    
    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
    

    
}
