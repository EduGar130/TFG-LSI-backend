package com.tfg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false)
    private String permissions;

    @Column(nullable = false)
    private Boolean isGlobal;

    public Role(String name, String permissions, Boolean isGlobal) {
        this.name = name;
        this.permissions = permissions;
        this.isGlobal = isGlobal;
    }
}
