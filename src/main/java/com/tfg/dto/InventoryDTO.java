package com.tfg.dto;

import lombok.Data;

@Data
public class InventoryDTO {
    private Long id;
    private ProductDTO product;
    private WarehouseDTO warehouse;
    private Integer quantity;
}
