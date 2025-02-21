package com.tfg.controller;

import com.tfg.dto.TransactionDTO;
import com.tfg.entity.Transaction;
import com.tfg.entity.TransactionType;
import com.tfg.mapper.InventoryMapper;
import com.tfg.mapper.TransactionMapper;
import com.tfg.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Listar todas las transacciones",
            description = "Devuelve un listado de todas las transacciones realizadas en el sistema, incluyendo entradas, salidas y ventas de productos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/get")
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions().stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Añadir entrada de stock (add)",
            description = "Registra una nueva entrada de producto en el sistema (tipo add). Requiere información del producto, almacén, cantidad, usuario y descripción opcional."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción de entrada registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/add")
    public TransactionDTO addTransaction(@RequestBody @Valid TransactionDTO dto) {
        return TransactionMapper.toDto(transactionService.saveTransaction(TransactionMapper.toEntity(dto)));
    }



}
