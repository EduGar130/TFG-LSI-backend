package com.tfg.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryAlertDTO {
    private Long id;
    private ProductDTO product;
    private WarehouseDTO warehouse;
    private String alertType;
    private String message;
    private LocalDateTime createdAt;
}
