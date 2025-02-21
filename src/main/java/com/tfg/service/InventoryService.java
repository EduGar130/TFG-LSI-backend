package com.tfg.service;

import com.tfg.entity.Inventory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public interface InventoryService {
    List<Inventory> getAllInventories();

    List<Inventory> getInventoryByWarehouse(Long warehouseId);

    void deleteInventoryById(Long id);

    Inventory addInventory(Inventory inventory);

    Inventory updateInventory(Long id, Inventory inventory);

    byte[] generateCsvFile();

    void importInventoryFromCsv(MultipartFile file);
}
