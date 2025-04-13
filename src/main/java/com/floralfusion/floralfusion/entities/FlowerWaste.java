package com.floralfusion.floralfusion.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import com.floralfusion.floralfusion.enums.FlowerType;
import com.floralfusion.floralfusion.enums.WasteStatus;

@Entity
@Table(name = "flower_waste")
public class FlowerWaste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wasteID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlowerType flowerType;  // Type of flower waste collected

    private int quantity;

    private LocalDate collectionDate;  

    @Enumerated(EnumType.STRING)
    private WasteStatus status;  // Enum for status (COLLECTED, DISTRIBUTED, PENDING)

    private String pickupLocation;
    private String area;
    private String city;
    private String pinCode;
    private String landmark;
    private LocalTime pickupTime;

    @ManyToOne
    @JoinColumn(name = "contributor_id", nullable = false)  
    private FlowerContributor flowerContributor;

    @ManyToOne
    @JoinColumn(name = "collector_id", nullable = true)  
    private Collector collector;

    @ManyToOne
    @JoinColumn(name = "flower_stock_id", nullable = false)  // Link to FlowerStock
    private FlowerStock flowerStock;

    // Getters and Setters
    public Long getWasteID() {
        return wasteID;
    }

    public void setWasteID(Long wasteID) {
        this.wasteID = wasteID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getCollectionDate() {  
        return collectionDate;
    }

    public void setCollectionDate(LocalDate collectionDate) {  
        this.collectionDate = collectionDate;
    }

    public WasteStatus getStatus() {
        return status;
    }

    public void setStatus(WasteStatus status) {
        this.status = status;
    }

    public FlowerContributor getFlowerContributor() {  
        return flowerContributor;
    }

    public void setFlowerContributor(FlowerContributor flowerContributor) {  
        this.flowerContributor = flowerContributor;
    }

    public Collector getCollector() {  
        return collector;
    }

    public void setCollector(Collector collector) {  
        this.collector = collector;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public FlowerStock getFlowerStock() {
        return flowerStock;
    }

    public void setFlowerStock(FlowerStock flowerStock) {
        this.flowerStock = flowerStock;
    }
}
