package com.tfg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_alerts")
@Data
@NoArgsConstructor
public class InventoryAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public InventoryAlert(Product product, Warehouse warehouse, String alertType, String message) {
        this.product = product;
        this.warehouse = warehouse;
        this.alertType = alertType;
        this.message = message;
    }

    @Override
    public String toString() {
        return "InventoryAlert{" +
                "id=" + id +
                ", productId=" + (product != null ? product.getId() : null) +
                ", warehouseId=" + (warehouse != null ? warehouse.getId() : null) +
                ", alertType='" + alertType + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
