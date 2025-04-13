package com.floralfusion.floralfusion.entities;

import java.util.List;
import com.floralfusion.floralfusion.enums.FlowerType;
import jakarta.persistence.*;

@Entity
@Table(name = "flower_stock")
public class FlowerStock {

    @Id
    @Enumerated(EnumType.STRING) // Using FlowerType as PK
    @Column(name = "flower_type", unique = true, nullable = false)
    private FlowerType flowerType;

    @Column(nullable = false)
    private int quantity; // Available stock in kg or pieces

    @OneToMany(mappedBy = "flowerStock", cascade = CascadeType.ALL)
    private List<FlowerWaste> flowerWastes;

    // Constructors
    public FlowerStock() {}

    public FlowerStock(FlowerType flowerType, int quantity) {
        this.flowerType = flowerType;
        this.quantity = quantity;
    }

    // Getters and Setters
    public FlowerType getFlowerType() {
        return flowerType;
    }

    public void setFlowerType(FlowerType flowerType) {
        this.flowerType = flowerType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
