package com.tfg.service.impl;

import com.tfg.entity.Inventory;
import com.tfg.entity.Product;
import com.tfg.entity.Warehouse;
import com.tfg.repository.InventoryRepository;
import com.tfg.repository.ProductRepository;
import com.tfg.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    private InventoryServiceImpl inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryServiceImpl(inventoryRepository, warehouseRepository, productRepository, null);
    }

    @Test
    void testGetAllInventories() {
        // Datos simulados
        Inventory inventory1 = new Inventory();
        inventory1.setId(1L);
        inventory1.setQuantity(10);

        Inventory inventory2 = new Inventory();
        inventory2.setId(2L);
        inventory2.setQuantity(20);

        List<Inventory> mockInventories = Arrays.asList(inventory1, inventory2);

        // Configuración del mock
        when(inventoryRepository.findAll()).thenReturn(mockInventories);

        // Ejecución del método
        List<Inventory> result = inventoryService.getAllInventories();

        // Verificación
        assertEquals(2, result.size());
        assertEquals(10, result.get(0).getQuantity());
        assertEquals(20, result.get(1).getQuantity());
    }

    @Test
    void testGetInventoryByWarehouse() {
        // Datos simulados
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Almacén 1");

        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity(50);

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(inventoryRepository.findByWarehouse(warehouse)).thenReturn(Arrays.asList(inventory));

        // Ejecución del método
        List<Inventory> result = inventoryService.getInventoryByWarehouse(1L);

        // Verificación
        assertEquals(1, result.size());
        assertEquals(50, result.get(0).getQuantity());
    }

    @Test
    void testDeleteInventoryById() {
        // Ejecución del método
        inventoryService.deleteInventoryById(1L);

        // Verificación
        verify(inventoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAddInventory() {
        // Datos simulados
        Product product = new Product();
        product.setId(1L);
        product.setName("Producto 1");

        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Almacén 1");

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity(100);

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // Ejecución del método
        Inventory result = inventoryService.addInventory(inventory);

        // Verificación
        assertEquals(100, result.getQuantity());
        assertEquals("Producto 1", result.getProduct().getName());
        assertEquals("Almacén 1", result.getWarehouse().getName());
    }
}