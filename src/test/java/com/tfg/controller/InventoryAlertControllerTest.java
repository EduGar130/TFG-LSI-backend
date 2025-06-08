package com.tfg.controller;

import com.tfg.entity.InventoryAlert;
import com.tfg.service.InventoryAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryAlertController.class)
class InventoryAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryAlertService alertService;

    @Test
    void testGetAllAlerts() throws Exception {
        when(alertService.getAllAlerts()).thenReturn(List.of(new InventoryAlert()));

        mockMvc.perform(get("/api/inventory-alerts/get"))
                .andExpect(status().isOk());
    }
}
