package com.tfg.controller;

import com.tfg.dto.AuditLogDTO;
import com.tfg.mapper.AuditLogMapper;
import com.tfg.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Operation(
            summary = "Obtener todos los logs de auditoría",
            description = "Este endpoint devuelve todos los registros del sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de registros obtenida con éxito"),
                    @ApiResponse(responseCode = "401", description = "No autorizado - se requiere token JWT"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public List<AuditLogDTO> getAllLogs() {
        return auditLogService.getAllLogs().stream()
                .map(AuditLogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Obtener logs por acción",
            description = "Devuelve los registros filtrados por el tipo de acción"
    )
    @GetMapping("/action/{action}")
    public List<AuditLogDTO> getLogsByAction(@PathVariable String action) {
        return auditLogService.getLogsByAction(action).stream()
                .map(AuditLogMapper::toDto)
                .collect(Collectors.toList());
    }
}
