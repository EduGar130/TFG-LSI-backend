package com.tfg.mapper;

import com.tfg.dto.InventoryAlertDTO;
import com.tfg.entity.InventoryAlert;

public class InventoryAlertMapper {

    public static InventoryAlertDTO toDto(InventoryAlert alert) {
        if (alert == null) return null;
        InventoryAlertDTO dto = new InventoryAlertDTO();
        dto.setId(alert.getId());
        dto.setProduct(ProductMapper.toDto(alert.getProduct()));
        dto.setAlertType(alert.getAlertType());
        dto.setMessage(alert.getMessage());
        dto.setCreatedAt(alert.getCreatedAt());
        return dto;
    }
}
