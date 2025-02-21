package com.tfg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, length = 50)
    private String sku;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_alert_threshold")
    private Integer stockAlertThreshold;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "product")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "product")
    private List<InventoryAlert> inventoryAlerts;

    public Product(String name, String sku, String description, BigDecimal price, int stockAlertThreshold, Category category) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.stockAlertThreshold = stockAlertThreshold;
        this.category = category;
    }
}
