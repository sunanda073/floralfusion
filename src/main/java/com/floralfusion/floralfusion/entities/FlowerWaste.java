package com.floralfusion.floralfusion.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "flower_waste")
public class FlowerWaste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wasteID;

    private int quantity;
    private String collectionDate;

    @ManyToOne
    @JoinColumn(name = "supplierID")
    private Supplier supplier;

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
    public String getCollectionDate() {
        return collectionDate;
    }
    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }
    public Supplier getSupplier() {
        return supplier;
    }
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
