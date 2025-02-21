package com.tfg.mapper;

import com.tfg.dto.InventoryDTO;
import com.tfg.entity.Inventory;

public class InventoryMapper {

    public static InventoryDTO toDto(Inventory inventory) {
        if (inventory == null) return null;
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setProduct(ProductMapper.toDto(inventory.getProduct()));
        dto.setWarehouse(WarehouseMapper.toDto(inventory.getWarehouse()));
        dto.setQuantity(inventory.getQuantity());
        return dto;
    }

    public static Inventory toEntity(InventoryDTO inventoryDTO) {
        if (inventoryDTO == null) return null;
        Inventory inventory = new Inventory();
        inventory.setId(inventoryDTO.getId());
        inventory.setProduct(ProductMapper.toEntity(inventoryDTO.getProduct()));
        inventory.setWarehouse(WarehouseMapper.toEntity(inventoryDTO.getWarehouse()));
        inventory.setQuantity(inventoryDTO.getQuantity());
        return inventory;
    }
}
