package com.floralfusion.floralfusion.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import java.util.List;

import com.floralfusion.floralfusion.enums.FlowerType;

@Entity
@Table(name = "flower_contributors")
public class FlowerContributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contributorID;

    private String type; // Type of the flower contributor (e.g., temple, market, individual)

    @Column(nullable = false)
    private int coins = 0;

    @Column(nullable = true)
    private String area;

    @Column(nullable = false)
    private boolean weeklyAutoPickup = false;

    @Column(nullable = true)
    private Double defaultWasteAmountInKg;

    @Column(length = 500)
    private String defaultPickupAddress;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private FlowerType defaultFlowerType;

    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    @OneToMany(mappedBy = "flowerContributor", fetch = FetchType.LAZY)
    private List<FlowerWaste> history;

    // Getters and Setters
    public Long getContributorID() {
        return contributorID;
    }

    public void setContributorID(Long contributorID) {
        this.contributorID = contributorID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<FlowerWaste> getHistory() {
        return history;
    }

    public void setHistory(List<FlowerWaste> history) {
        this.history = history;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isWeeklyAutoPickup() {
        return weeklyAutoPickup;
    }

    public void setWeeklyAutoPickup(boolean weeklyAutoPickup) {
        this.weeklyAutoPickup = weeklyAutoPickup;
    }

    public Double getDefaultWasteAmountInKg() {
        return defaultWasteAmountInKg;
    }

    public void setDefaultWasteAmountInKg(Double defaultWasteAmountInKg) {
        this.defaultWasteAmountInKg = defaultWasteAmountInKg;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int additionalCoins) {
        this.coins += additionalCoins;
    }

    public String getDefaultPickupAddress() {
        return defaultPickupAddress;
    }

    public void setDefaultPickupAddress(String defaultPickupAddress) {
        this.defaultPickupAddress = defaultPickupAddress;
    }

    public FlowerType getDefaultFlowerType() {
        return defaultFlowerType;
    }
    
    public void setDefaultFlowerType(FlowerType defaultFlowerType) {
        this.defaultFlowerType = defaultFlowerType;
    }
    
}
