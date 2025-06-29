package com.tfg.controller;

import com.tfg.dto.InventoryAlertDTO;
import com.tfg.exception.GlobalExceptionHandler;
import com.tfg.exception.UnauthorizedException;
import com.tfg.mapper.InventoryAlertMapper;
import com.tfg.security.config.RequiresPermission;
import com.tfg.service.InventoryAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory-alerts")
@SecurityRequirement(name = "bearerAuth")
public class InventoryAlertController {

    private final InventoryAlertService alertService;
    private final GlobalExceptionHandler globalExceptionHandler;

    public InventoryAlertController(InventoryAlertService alertService, GlobalExceptionHandler globalExceptionHandler) {
        this.alertService = alertService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Operation(
            summary = "Listar alertas de inventario",
            description = "Devuelve todas las alertas registradas en el sistema sobre el estado del inventario de productos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de alertas obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "[{\"id\": 1, \"productName\": \"Producto A\", \"alertType\": \"STOCK_BAJO\", \"warehouseId\": 101}, {\"id\": 2, \"productName\": \"Producto B\", \"alertType\": \"STOCK_AGOTADO\", \"warehouseId\": 102}]")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado – token JWT ausente o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Token JWT inválido o ausente.\"}")
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
    @RequiresPermission("view_alerts")
    @GetMapping("/get")
    public ResponseEntity<?> getAllAlerts() {
        try {
            return ResponseEntity.ok(
                    alertService.getAllAlerts().stream()
                            .map(InventoryAlertMapper::toDto)
                            .collect(Collectors.toList())
            );
        }catch (UnauthorizedException ex) {
            return globalExceptionHandler.handleUnauthorized(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }
}
