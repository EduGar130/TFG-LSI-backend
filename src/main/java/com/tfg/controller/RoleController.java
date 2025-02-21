package com.tfg.controller;

import com.tfg.dto.RoleDTO;
import com.tfg.entity.Role;
import com.tfg.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(
            summary = "Listar todos los roles",
            description = "Devuelve una lista con todos los roles registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Operation(
            summary = "Obtener un rol por ID",
            description = "Devuelve los datos del rol correspondiente al ID proporcionado, si existe."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public RoleDTO getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @Operation(
            summary = "Crear un nuevo rol",
            description = "Registra un nuevo rol en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/add")
    public RoleDTO createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @Operation(
            summary = "Actualizar un rol",
            description = "Actualiza la información de un rol existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/update")
    public RoleDTO updateRole(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @Operation(
            summary = "Eliminar un rol",
            description = "Elimina un rol del sistema por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRoleById(id);
    }
}