package com.tfg.controller;

import com.tfg.dto.InventoryDTO;
import com.tfg.mapper.InventoryMapper;
import com.tfg.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
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

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(
            summary = "Listar inventario",
            description = "Devuelve la lista de existencias de productos por almacén."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get")
    public List<InventoryDTO> getAllInventory() {
        return inventoryService.getAllInventories().stream()
                .map(InventoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Listar inventario por almacén",
            description = "Devuelve la lista de existencias de productos por almacén."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get/{warehouseId}")
    public List<InventoryDTO> getInventoryByWarehouse(@PathVariable Long warehouseId) {
        return inventoryService.getInventoryByWarehouse(warehouseId).stream()
                .map(InventoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Eliminar inventario",
            description = "Elimina un inventario específico por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/delete/{id}")
    public void deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventoryById(id);
    }

    @Operation(
            summary = "Añadir producto al inventario",
            description = "Añade un nuevo producto al inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto añadido correctamente al inventario"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta – datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/add")
    public InventoryDTO addInventory(@RequestBody InventoryDTO inventoryDTO) {
        return InventoryMapper.toDto(inventoryService.addInventory(InventoryMapper.toEntity(inventoryDTO)));
    }

    @Operation(
            summary = "Actualizar producto en el inventario",
            description = "Actualiza la información de un producto existente en el inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente en el inventario"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta – datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/update/{id}")
    public InventoryDTO updateInventory(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        return InventoryMapper.toDto(inventoryService.updateInventory(id, InventoryMapper.toEntity(inventoryDTO)));
    }

    //Genera un CSV con el inventario
    @Operation(
            summary = "Generar CSV de inventario",
            description = "Genera un archivo CSV con la lista de productos en el inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
            @ApiResponse(responseCode = "200", description = "Inventario importado correctamente"),
            @ApiResponse(responseCode = "400", description = "CSV mal formado o datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
