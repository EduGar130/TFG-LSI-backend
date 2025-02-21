package com.tfg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "transaction_type", nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Transaction(Product product, Warehouse warehouse, User user, TransactionType type, int quantity, String description, LocalDateTime createdAt) {
        this.product = product;
        this.warehouse = warehouse;
        this.user = user;
        this.type = type;
        this.quantity = quantity;
        this.description = description;
        this.createdAt = createdAt;
    }
}
