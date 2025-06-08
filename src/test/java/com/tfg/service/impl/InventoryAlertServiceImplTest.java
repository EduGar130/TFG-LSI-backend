package com.tfg.service.impl;

import com.tfg.entity.InventoryAlert;
import com.tfg.repository.InventoryAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class InventoryAlertServiceImplTest {

    private InventoryAlertServiceImpl inventoryAlertService;

    @Mock
    private InventoryAlertRepository alertRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryAlertService = new InventoryAlertServiceImpl(alertRepository);
    }

    @Test
    void testGetAllAlerts() {
        // Datos simulados
        InventoryAlert alert1 = new InventoryAlert();
        alert1.setId(1L);
        alert1.setMessage("Alerta 1");

        InventoryAlert alert2 = new InventoryAlert();
        alert2.setId(2L);
        alert2.setMessage("Alerta 2");

        List<InventoryAlert> mockAlerts = Arrays.asList(alert1, alert2);

        // Configuración del mock
        when(alertRepository.findAll()).thenReturn(mockAlerts);

        // Ejecución del método
        List<InventoryAlert> result = inventoryAlertService.getAllAlerts();

        // Verificación
        assertEquals(2, result.size());
        assertEquals("Alerta 1", result.get(0).getMessage());
        assertEquals("Alerta 2", result.get(1).getMessage());
    }
}