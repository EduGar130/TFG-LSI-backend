package com.tfg.controller;

import com.tfg.dto.EstadisticasDTO;
import com.tfg.exception.GlobalExceptionHandler;
import com.tfg.security.config.RequiresPermission;
import com.tfg.service.EstadisticasService;
import com.tfg.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    @Autowired
    private EstadisticasService estadisticasService;

    @Autowired
    private ReportService reportService;

    @Operation(
        summary = "Descargar reporte de estadísticas en formato PDF",
        description = "Genera y descarga un archivo PDF con las estadísticas basadas en los parámetros proporcionados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "PDF generado y descargado correctamente",
            content = @Content(
                mediaType = "application/pdf",
                schema = @Schema(example = "Archivo PDF generado con éxito.")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos o incompletos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Parámetros inválidos o incompletos.\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Ha ocurrido un error inesperado.\"}")
            )
        )
    })
    @RequiresPermission("view_reports")
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
