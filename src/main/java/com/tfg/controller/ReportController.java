package com.tfg.controller;

import com.tfg.dto.EstadisticasDTO;
import com.tfg.service.EstadisticasService;
import com.tfg.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private EstadisticasService estadisticasService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/estadisticas/pdf")
    public ResponseEntity<byte[]> descargarPDF(@RequestParam(required = false) List<String> sku, @RequestParam(required = false) String almacen, @RequestParam(required = false) String categoria, @RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin) {
        try {
            EstadisticasDTO dto = estadisticasService.obtenerEstadisticas(sku.get(0), almacen, categoria, fechaInicio, fechaFin);
            byte[] pdf = reportService.generarEstadisticasPDF(dto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("reporte_estadisticas.pdf")
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
