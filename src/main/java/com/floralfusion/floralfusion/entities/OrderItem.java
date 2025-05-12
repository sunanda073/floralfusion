package com.floralfusion.floralfusion.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemID;

    private int quantity;
    private double unitPrice;  // Unit price of the product
    private double totalPrice;  // Total price (unitPrice * quantity)

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;  // Relationship with Product

    @ManyToOne
    @JoinColumn(name = "orderID")
    private CustomerOrder order;  // Relationship with Order

    // Constructors
    public OrderItem(int quantity, double unitPrice, Product product, CustomerOrder order) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity; // Calculate totalPrice
        this.product = product;
        this.order = order;
    }

    // Default constructor (no-arg constructor)
    public OrderItem() {
    }

    // Getters and Setters
    public Long getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(Long orderItemID) {
        this.orderItemID = orderItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.unitPrice * quantity; // Update totalPrice when quantity changes
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * this.quantity; // Update totalPrice when unitPrice changes
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }
}
