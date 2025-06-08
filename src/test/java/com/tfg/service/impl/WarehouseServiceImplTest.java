package com.tfg.service.impl;

import com.tfg.entity.Warehouse;
import com.tfg.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WarehouseServiceImplTest {

    private WarehouseServiceImpl warehouseService;

    @Mock
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        warehouseService = new WarehouseServiceImpl(warehouseRepository);
    }

    @Test
    void testGetAllWarehouses() {
        // Datos simulados
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setId(1L);
        warehouse1.setName("Almacén 1");

        Warehouse warehouse2 = new Warehouse();
        warehouse2.setId(2L);
        warehouse2.setName("Almacén 2");

        List<Warehouse> mockWarehouses = Arrays.asList(warehouse1, warehouse2);

        // Configuración del mock
        when(warehouseRepository.findAll()).thenReturn(mockWarehouses);

        // Ejecución del método
        List<Warehouse> result = warehouseService.getAllWarehouses();

        // Verificación
        assertEquals(2, result.size());
        assertEquals("Almacén 1", result.get(0).getName());
        assertEquals("Almacén 2", result.get(1).getName());
    }

    @Test
    void testCheckProductInWarehouse() {
        // Configuración del mock
        when(warehouseRepository.checkProductInWarehouse(1L, 1L)).thenReturn(1);
        when(warehouseRepository.checkProductInWarehouse(1L, 2L)).thenReturn(0);

        // Ejecución del método
        boolean result1 = warehouseService.checkProductInWarehouse(1L, 1L);
        boolean result2 = warehouseService.checkProductInWarehouse(1L, 2L);

        // Verificación
        assertTrue(result1);
        assertFalse(result2);
    }
}