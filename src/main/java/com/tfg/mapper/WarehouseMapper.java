package com.tfg.mapper;

import com.tfg.dto.WarehouseDTO;
import com.tfg.entity.Warehouse;

public class WarehouseMapper {

    public static WarehouseDTO toDto(Warehouse warehouse) {
        if (warehouse == null) return null;
        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(warehouse.getId());
        dto.setName(warehouse.getName());
        dto.setLocation(warehouse.getLocation());
        return dto;
    }

    public static Warehouse toEntity(WarehouseDTO warehouse) {
        if (warehouse == null) return null;
        Warehouse entity = new Warehouse();
        entity.setId(warehouse.getId());
        entity.setName(warehouse.getName());
        entity.setLocation(warehouse.getLocation());
        return entity;
    }
}
