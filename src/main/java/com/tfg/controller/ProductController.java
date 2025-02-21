package com.tfg.controller;

import com.tfg.dto.ProductDTO;
import com.tfg.entity.Product;
import com.tfg.exception.ResourceNotFoundException;
import com.tfg.mapper.ProductMapper;
import com.tfg.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Listar todos los productos",
            description = "Devuelve una lista de todos los productos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Obtener un producto por ID",
            description = "Devuelve el producto correspondiente al ID proporcionado, si existe."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Producto no encontrado con ID " + id);
        }
        return ProductMapper.toDto(product);
    }
}
