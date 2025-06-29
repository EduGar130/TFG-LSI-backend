package com.tfg.controller;

import com.tfg.dto.CategoryDTO;
import com.tfg.exception.GlobalExceptionHandler;
import com.tfg.exception.UnauthorizedException;
import com.tfg.mapper.CategoryMapper;
import com.tfg.service.CategoryService;
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
@RequestMapping("/api/categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final CategoryService categoryService;
    private final GlobalExceptionHandler globalExceptionHandler;

    public CategoryController(CategoryService categoryService, GlobalExceptionHandler globalExceptionHandler) {
        this.categoryService = categoryService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Operation(
            summary = "Obtener todas las categorías",
            description = "Devuelve una lista de todas las categorías registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categorías obtenidas correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "[{\"id\": 1, \"name\": \"Electrónica\"}, {\"id\": 2, \"name\": \"Ropa\"}]")
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
    public ResponseEntity<?> getAllCategories() {
        try {
            return ResponseEntity.ok(
                    categoryService.getAllCategories().stream()
                            .map(CategoryMapper::toDto)
                            .collect(Collectors.toList())
            );
        } catch (UnauthorizedException ex) {
            return globalExceptionHandler.handleUnauthorized(ex);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }
}
