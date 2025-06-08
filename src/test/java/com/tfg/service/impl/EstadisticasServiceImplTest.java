package com.tfg.service.impl;

import com.tfg.dto.EstadisticasDTO;
import com.tfg.entity.*;
import com.tfg.repository.CategoryRepository;
import com.tfg.repository.TransactionRepository;
import com.tfg.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class EstadisticasServiceImplTest {

    private EstadisticasServiceImpl estadisticasService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        estadisticasService = new EstadisticasServiceImpl(transactionRepository, warehouseRepository, categoryRepository);
    }

    @Test
    void testObtenerEstadisticas() {
        // Datos simulados
        Product product1 = new Product();
        product1.setCategory(new Category());
        product1.getCategory().setId(1L);
        product1.getCategory().setName("Categoría 1");
        User user = new User();
        user.setUsername("usuario1");

        Transaction transaction1 = new Transaction();
        transaction1.setType(TransactionType.SALE);
        transaction1.setQuantity(10);
        transaction1.setCreatedAt(LocalDateTime.now());
        transaction1.setProduct(product1);
        Warehouse warehouse = new Warehouse();
        transaction1.setUser(user);
        warehouse.setId(1L);
        warehouse.setName("Almacén 1");
        transaction1.setWarehouse(warehouse);

        Product product2 = new Product();
        product2.setCategory(new Category());
        product2.getCategory().setId(1L);
        product2.getCategory().setName("Categoría 1");

        Transaction transaction2 = new Transaction();
        transaction2.setType(TransactionType.SALE);
        transaction2.setQuantity(5);
        transaction2.setCreatedAt(LocalDateTime.now());
        transaction2.setProduct(product2);
        transaction2.setUser(user);
        transaction2.setWarehouse(warehouse);

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(product1.getCategory()));

        // Ejecución del método
        EstadisticasDTO result = estadisticasService.obtenerEstadisticas(null, "1", "1", null, null);

        // Verificación
        assertNotNull(result);
        assertNotNull(result.getVentasPorCategoria());
        assertNotNull(result.getMovimientosPorTipo());
    }
}