package com.tfg.controller;

import com.tfg.dto.InventoryDTO;
import com.tfg.exception.GlobalExceptionHandler;
import com.tfg.exception.UnauthorizedException;
import com.tfg.mapper.InventoryMapper;
import com.tfg.security.config.RequiresPermission;
import com.tfg.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@SecurityRequirement(name = "bearerAuth")
public class InventoryController {

    private final InventoryService inventoryService;
    private final GlobalExceptionHandler globalExceptionHandler;

    public InventoryController(InventoryService inventoryService, GlobalExceptionHandler globalExceptionHandler) {
        this.inventoryService = inventoryService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Operation(
            summary = "Listar inventario",
            description = "Devuelve la lista de existencias de productos por almacén."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "[{\"id\": 1, \"productName\": \"Producto A\", \"quantity\": 100, \"warehouseId\": 101}, {\"id\": 2, \"productName\": \"Producto B\", \"quantity\": 50, \"warehouseId\": 102}]")
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
                    responseCode = "403",
                    description = "Acceso denegado – permisos insuficientes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Permisos insuficientes para acceder a este recurso.\"}")
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
    @GetMapping("/get")
    public ResponseEntity<?> getAllInventory() {
        try {
            return ResponseEntity.ok(
                    inventoryService.getAllInventories().stream()
                            .map(InventoryMapper::toDto)
                            .collect(Collectors.toList())
            );
        } catch (UnauthorizedException ex) {
            return globalExceptionHandler.handleUnauthorized(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Listar inventario por almacén",
            description = "Devuelve la lista de existencias de productos por almacén."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "[{\"id\": 1, \"productName\": \"Producto A\", \"quantity\": 100, \"warehouseId\": 101}, {\"id\": 2, \"productName\": \"Producto B\", \"quantity\": 50, \"warehouseId\": 102}]")
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
                    responseCode = "403",
                    description = "Acceso denegado – permisos insuficientes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Permisos insuficientes para acceder a este recurso.\"}")
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
    @GetMapping("/get/{warehouseId}")
    public ResponseEntity<?> getInventoryByWarehouse(@PathVariable Long warehouseId) {
        try {
            return ResponseEntity.ok(
                    inventoryService.getInventoryByWarehouse(warehouseId).stream()
                            .map(InventoryMapper::toDto)
                            .collect(Collectors.toList())
            );
        } catch (UnauthorizedException ex) {
            return globalExceptionHandler.handleUnauthorized(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Eliminar inventario",
            description = "Elimina un inventario específico por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario eliminado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Inventario eliminado correctamente\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Inventario no encontrado.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado – permisos insuficientes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Permisos insuficientes para acceder a este recurso.\"}")
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
    @RequiresPermission("manage_inventory")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        try {
            inventoryService.deleteInventoryById(id);
            return ResponseEntity.ok("Inventario eliminado correctamente");
        } catch (UnauthorizedException ex) {
            return globalExceptionHandler.handleUnauthorized(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Añadir producto al inventario",
            description = "Añade un nuevo producto al inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto añadido correctamente al inventario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Producto añadido correctamente al inventario\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud incorrecta – datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Datos inválidos en la solicitud.\"}")
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
                    responseCode = "403",
                    description = "Acceso denegado – permisos insuficientes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Permisos insuficientes para acceder a este recurso.\"}")
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
    @RequiresPermission("manage_inventory")
    @PostMapping("/add")
    public ResponseEntity<?> addInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(InventoryMapper.toDto(inventoryService.addInventory(InventoryMapper.toEntity(inventoryDTO))));
        } catch (UnauthorizedException ex) {
            return globalExceptionHandler.handleUnauthorized(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Actualizar producto en el inventario",
            description = "Actualiza la información de un producto existente en el inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente en el inventario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Producto actualizado correctamente en el inventario\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Producto no encontrado.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud incorrecta – datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Datos inválidos en la solicitud.\"}")
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
                    responseCode = "403",
                    description = "Acceso denegado – permisos insuficientes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Permisos insuficientes para acceder a este recurso.\"}")
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
    @RequiresPermission("manage_inventory")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        try {
            return ResponseEntity.ok(
                    InventoryMapper.toDto(inventoryService.updateInventory(id, InventoryMapper.toEntity(inventoryDTO))));
        } catch (UnauthorizedException ex) {
            return globalExceptionHandler.handleUnauthorized(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Generar CSV de inventario",
            description = "Genera un archivo CSV con la lista de productos en el inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV generado correctamente",
                    content = @Content(
                            mediaType = "text/csv",
                            schema = @Schema(example = "id,productName,quantity,warehouseId\n1,Producto A,100,101\n2,Producto B,50,102")
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
                    responseCode = "403",
                    description = "Acceso denegado – permisos insuficientes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Permisos insuficientes para acceder a este recurso.\"}")
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
    @RequiresPermission("view_inventory")
    @GetMapping("/generate-csv")
    public ResponseEntity<byte[]> generateInventoryCsv() {
        try {
            byte[] csvBytes = inventoryService.generateCsvFile();
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"inventario.csv\"");
            headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Importar CSV de inventario",
            description = "Carga un archivo CSV para añadir registros al inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario importado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"CSV importado correctamente\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "CSV mal formado o datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"El archivo CSV está mal formado o contiene datos inválidos.\"}")
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
                    responseCode = "403",
                    description = "Acceso denegado – permisos insuficientes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Permisos insuficientes para acceder a este recurso.\"}")
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
    @RequiresPermission("manage_inventory")
    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        try {
            inventoryService.importInventoryFromCsv(file);
            return ResponseEntity.ok("CSV importado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al importar el CSV: " + e.getMessage());
        }
    }


}
