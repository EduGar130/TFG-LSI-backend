package com.tfg.dto;

import com.tfg.entity.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private ProductDTO product;
    private WarehouseDTO warehouse;
    private UserDTO user;
    private TransactionType type;
    private Integer quantity;
    private String description;
    private LocalDateTime createdAt;
}
