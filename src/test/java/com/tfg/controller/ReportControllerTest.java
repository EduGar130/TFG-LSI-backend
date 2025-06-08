package com.tfg.controller;

import com.tfg.dto.EstadisticasDTO;
import com.tfg.service.EstadisticasService;
import com.tfg.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadisticasService estadisticasService;

    @MockBean
    private ReportService reportService;

    @Test
    void testDescargarPDF() throws Exception {
        when(estadisticasService.obtenerEstadisticas(anyString(), any(), any(), any(), any()))
                .thenReturn(new EstadisticasDTO());
        when(reportService.generarEstadisticasPDF(any(EstadisticasDTO.class)))
                .thenReturn(new byte[]{1,2,3});

        mockMvc.perform(get("/api/reports/estadisticas/pdf")
                .param("sku", "ABC")
                .param("almacen", "1")
                .param("categoria", "CAT")
                .param("fechaInicio", "2024-01-01")
                .param("fechaFin", "2024-02-01"))
                .andExpect(status().isOk());
    }
}
