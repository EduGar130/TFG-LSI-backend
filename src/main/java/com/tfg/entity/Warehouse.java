package com.tfg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "warehouses")
@Data
@NoArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String location;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "warehouse")
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "warehouse")
    private List<Transaction> transactions;

    public Warehouse(String name, String location) {
        this.name = name;
        this.location = location;
    }
}
