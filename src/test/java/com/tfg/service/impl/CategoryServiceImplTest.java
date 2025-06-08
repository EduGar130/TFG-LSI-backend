package com.tfg.service.impl;

import com.tfg.entity.Category;
import com.tfg.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CategoryServiceImplTest {

    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void testGetAllCategories() {
        // Datos simulados
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        List<Category> mockCategories = Arrays.asList(category1, category2);

        // Configuración del mock
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        // Ejecución del método
        List<Category> result = categoryService.getAllCategories();

        // Verificación
        assertEquals(2, result.size());
        assertEquals("Category 1", result.get(0).getName());
        assertEquals("Category 2", result.get(1).getName());
    }
}