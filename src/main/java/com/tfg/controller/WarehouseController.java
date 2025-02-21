package com.tfg.controller;

import com.tfg.dto.WarehouseDTO;
import com.tfg.mapper.WarehouseMapper;
import com.tfg.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
@SecurityRequirement(name = "bearerAuth")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Operation(
            summary = "Listar almacenes",
            description = "Devuelve un listado de todos los almacenes registrados en el sistema, incluyendo nombre y localización."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de almacenes obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – se requiere token JWT"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get")
    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseService.getAllWarehouses().stream()
                .map(WarehouseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Comprobar si un producto está en un almacén",
            description = "Devuelve true si el producto ya está en el almacén, false en caso contrario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprobación realizada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – se requiere token JWT"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/check/{warehouseId}/{productId}")
    public boolean checkProductInWarehouse(@PathVariable Long warehouseId, @PathVariable Long productId) {
        return warehouseService.checkProductInWarehouse(warehouseId, productId);
    }
}
