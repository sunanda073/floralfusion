package com.floralfusion.floralfusion.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminID;

    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    @Column(nullable = false)
    private String status;  

    @Column(nullable = false)
    private String dept;  

    @Column(nullable = false, unique = true)  // Ensuring passkey is required and unique
    private String passkey;    

    @OneToMany
    @JoinColumn(name = "adminID") // This column will reference the adminID in the Collector table
    private List<Collector> managedCollectors;  

    // Getters and Setters
    public Long getAdminID() {
        return adminID;
    }

    public void setAdminID(Long adminID) {
        this.adminID = adminID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public List<Collector> getManagedCollectors() {
        return managedCollectors;
    }

    public void setManagedCollectors(List<Collector> managedCollectors) {
        this.managedCollectors = managedCollectors;
    }
}
