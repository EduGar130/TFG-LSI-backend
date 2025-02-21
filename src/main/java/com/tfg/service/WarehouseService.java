package com.tfg.service;

import com.tfg.entity.Warehouse;

import java.util.List;

public interface WarehouseService {
    List<Warehouse> getAllWarehouses();

    boolean checkProductInWarehouse(Long warehouseId, Long productId);
}
