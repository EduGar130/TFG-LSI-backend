package com.tfg.service.impl;

import com.tfg.entity.*;
import com.tfg.exception.StockInsuficienteException;
import com.tfg.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InventoryAlertRepository inventoryAlertRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionRepository, inventoryRepository, warehouseRepository, productRepository, userRepository, inventoryAlertRepository);
    }

    @Test
    void testSaveTransactionWithSufficientStock() {
        // Datos simulados
        Product product = new Product();
        product.setId(1L);
        product.setName("Producto 1");
        product.setStockAlertThreshold(5);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Almacén 1");

        User user = new User();
        user.setUsername("usuario1");

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.SALE);
        transaction.setProduct(product);
        transaction.setWarehouse(warehouse);
        transaction.setUser(user); // Asegúrate de inicializar el usuario
        transaction.setQuantity(3);

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("usuario1")).thenReturn(Optional.of(user));
        when(inventoryRepository.tiene(1L, 3, 1L)).thenReturn(10);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Ejecución del método
        Transaction result = transactionService.saveTransaction(transaction);

        // Verificación
        assertNotNull(result); // Verifica que el resultado no sea null
        verify(inventoryRepository, times(1)).restarCantidad(1L, 1L, 3);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testSaveTransactionWithInsufficientStock() {
        // Datos simulados
        Product product = new Product();
        product.setId(1L);
        product.setName("Producto 1");

        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);

        User user = new User();
        user.setUsername("usuario1");

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.SALE);
        transaction.setProduct(product);
        transaction.setWarehouse(warehouse);
        transaction.setUser(user); // Asegúrate de inicializar el usuario
        transaction.setQuantity(10);

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("usuario1")).thenReturn(Optional.of(user));
        when(inventoryRepository.tiene(1L, 10, 1L)).thenReturn(5);

        // Ejecución y verificación
        assertThrows(StockInsuficienteException.class, () -> transactionService.saveTransaction(transaction));
        verify(inventoryRepository, never()).restarCantidad(anyLong(), anyLong(), anyInt());
    }
}