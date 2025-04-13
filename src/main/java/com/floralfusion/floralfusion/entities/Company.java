package com.floralfusion.floralfusion.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "company_representatives")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyID;

    private String companyName;
    private String companyAddress;
    private String position;
    private String companyPhone;
    private String website;
    private String taxID;

    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    // One-to-many relationship with Product entity (Company produces many products)
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> productHistory;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CollectionRequest> collectionRequests;

    //No argument constructor
    public Company() {
    }

    // Constructor with parameters
    public Company(String companyName, String companyAddress, String position, String companyPhone, String industryType, String website, String taxID, User user) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.position = position;
        this.companyPhone = companyPhone;
        this.website = website;
        this.taxID = taxID;
        this.user = user;
    }

    // Getters and Setters
    public Long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Long companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProductHistory() {
        return productHistory;
    }

    public void setProductHistory(List<Product> productHistory) {
        this.productHistory = productHistory;
    }

    public void addCollectionRequest(CollectionRequest request) {
        collectionRequests.add(request);
        request.setCompany(this); 
    }

    public void removeCollectionRequest(CollectionRequest request) {
        collectionRequests.remove(request);
        request.setCompany(null);
    }
}
