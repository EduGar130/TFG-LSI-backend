package com.tfg.controller;

import com.tfg.dto.ProductDTO;
import com.tfg.entity.Product;
import com.tfg.exception.GlobalExceptionHandler;
import com.tfg.exception.ResourceNotFoundException;
import com.tfg.mapper.ProductMapper;
import com.tfg.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final GlobalExceptionHandler globalExceptionHandler;

    private final ProductService productService;

    public ProductController(GlobalExceptionHandler globalExceptionHandler, ProductService productService) {
        this.globalExceptionHandler = globalExceptionHandler;
        this.productService = productService;
    }

    @Operation(
            summary = "Listar todos los productos",
            description = "Devuelve una lista de todos los productos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos obtenidos correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "[{\"id\": 1, \"name\": \"Producto A\", \"price\": 100.0}, {\"id\": 2, \"name\": \"Producto B\", \"price\": 200.0}]")
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
    @GetMapping("/get")
    public ResponseEntity<?> getAllProducts() {
        try {
            return ResponseEntity.ok(
                    productService.getAllProducts().stream()
                            .map(ProductMapper::toDto)
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Obtener un producto por ID",
            description = "Devuelve el producto correspondiente al ID proporcionado, si existe."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"id\": 1, \"name\": \"Producto A\", \"price\": 100.0}")
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
                    responseCode = "404",
                    description = "Producto no encontrado con el ID especificado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Producto no encontrado con el ID especificado.\"}")
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
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                throw new ResourceNotFoundException("Producto no encontrado con ID " + id);
            }
            return ResponseEntity.ok(ProductMapper.toDto(product));
        } catch (ResourceNotFoundException e) {
            return globalExceptionHandler.handleResourceNotFound(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }
}
