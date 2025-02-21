package com.tfg.controller;

import com.tfg.dto.InventoryAlertDTO;
import com.tfg.mapper.InventoryAlertMapper;
import com.tfg.service.InventoryAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory-alerts")
@SecurityRequirement(name = "bearerAuth")
public class InventoryAlertController {

    private final InventoryAlertService alertService;

    public InventoryAlertController(InventoryAlertService alertService) {
        this.alertService = alertService;
    }

    @Operation(
            summary = "Listar alertas de inventario",
            description = "Devuelve todas las alertas registradas en el sistema sobre el estado del inventario de productos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de alertas obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get")
    public List<InventoryAlertDTO> getAllAlerts() {
        return alertService.getAllAlerts().stream()
                .map(InventoryAlertMapper::toDto)
                .collect(Collectors.toList());
    }
}
