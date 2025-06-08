package com.tfg.controller;

import com.tfg.entity.Warehouse;
import com.tfg.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    @Test
    void testGetAllWarehouses() throws Exception {
        when(warehouseService.getAllWarehouses()).thenReturn(List.of(new Warehouse()));

        mockMvc.perform(get("/api/warehouses/get"))
                .andExpect(status().isOk());
    }
}
