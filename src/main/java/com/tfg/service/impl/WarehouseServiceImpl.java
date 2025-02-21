package com.tfg.service.impl;

import com.tfg.entity.Warehouse;
import com.tfg.repository.WarehouseRepository;
import com.tfg.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Override
    public boolean checkProductInWarehouse(Long warehouseId, Long productId) {

        int a = warehouseRepository.checkProductInWarehouse(warehouseId, productId);
        return a != 0;
    }
}
