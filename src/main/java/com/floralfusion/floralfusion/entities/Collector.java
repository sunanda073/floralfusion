package com.floralfusion.floralfusion.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "collectors")
public class Collector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collectorID;

    @OneToOne
    @JoinColumn(name = "userID")  
    private User user;  

    @ManyToOne
    @JoinColumn(name = "adminID")  
    private Admin admin;  

    private String area;  
    private double rating; 
    
    @Column(nullable = false)
    private int isAvailable = 1;

    @OneToMany(mappedBy = "collector")  
    private List<FlowerWaste> history;  



    // Getters and Setters
    public Long getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(Long collectorID) {
        this.collectorID = collectorID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<FlowerWaste> getHistory() {
        return history;
    }

    public void setHistory(List<FlowerWaste> history) {
        this.history = history;
    }

    public int isAvailable() {
        return isAvailable;
    }

    public void setAvailable(int available) {
        isAvailable = available;
    }
}
