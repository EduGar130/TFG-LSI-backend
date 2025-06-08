package com.tfg.service.impl;

import com.tfg.entity.Product;
import com.tfg.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void testGetAllProducts() {
        // Datos simulados
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Producto 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Producto 2");

        List<Product> mockProducts = Arrays.asList(product1, product2);

        // Configuración del mock
        when(productRepository.findAll()).thenReturn(mockProducts);

        // Ejecución del método
        List<Product> result = productService.getAllProducts();

        // Verificación
        assertEquals(2, result.size());
        assertEquals("Producto 1", result.get(0).getName());
        assertEquals("Producto 2", result.get(1).getName());
    }

    @Test
    void testGetProductById() {
        // Datos simulados
        Product product = new Product();
        product.setId(1L);
        product.setName("Producto 1");

        // Configuración del mock
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        // Ejecución del método
        Product result1 = productService.getProductById(1L);
        Product result2 = productService.getProductById(2L);

        // Verificación
        assertEquals("Producto 1", result1.getName());
        assertNull(result2);
    }
}