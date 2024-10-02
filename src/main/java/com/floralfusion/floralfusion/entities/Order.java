package com.floralfusion.floralfusion.entities;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    private String orderDate;
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

    @ManyToMany
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "orderID"),
        inverseJoinColumns = @JoinColumn(name = "productID")
    )
    private List<Product> products;

    // Getters and Setters
    public Long getOrderID() {
        return orderID;
    }
    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public List<Product> getProducts() { 
        return products;
    }
    public void setProducts(List<Product> products) { 
        this.products = products;
    }
}
