package com.tfg.controller;

import com.tfg.dto.UserDTO;
import com.tfg.entity.User;
import com.tfg.exception.GlobalExceptionHandler;
import com.tfg.exception.ResourceNotFoundException;
import com.tfg.exception.UnauthorizedException;
import com.tfg.mapper.UserMapper;
import com.tfg.security.config.RequiresPermission;
import com.tfg.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    private final GlobalExceptionHandler globalExceptionHandler;

    public UserController(UserService userService, GlobalExceptionHandler globalExceptionHandler) {
        this.userService = userService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Operation(
            summary = "Listar todos los usuarios",
            description = "Devuelve una lista con todos los usuarios registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuarios obtenidos correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "[{\"id\": 1, \"name\": \"Juan Pérez\", \"email\": \"juan.perez@example.com\"}, {\"id\": 2, \"name\": \"Ana López\", \"email\": \"ana.lopez@example.com\"}]")
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
    @RequiresPermission("create_users")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(
                    userService.getAllUsers().stream()
                            .map(UserMapper::toDto)
                            .collect(Collectors.toList())
            );
        } catch (UnauthorizedException e) {
            return globalExceptionHandler.handleUnauthorized(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Obtener un usuario por ID",
            description = "Devuelve los datos del usuario correspondiente al ID proporcionado, si existe."
    )
   @ApiResponses(value = {
           @ApiResponse(
                   responseCode = "200",
                   description = "Usuario encontrado correctamente",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(example = "{\"id\": 1, \"name\": \"Juan Pérez\", \"email\": \"juan.perez@example.com\"}")
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
                   description = "Usuario no encontrado con el ID especificado",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuario no encontrado con el ID especificado.\"}")
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
    @RequiresPermission("create_users")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                throw new ResourceNotFoundException("Usuario no encontrado con ID " + id);
            }
            return ResponseEntity.ok(UserMapper.toDto(user));
        } catch (UnauthorizedException e) {
            return globalExceptionHandler.handleUnauthorized(e);
        } catch (ResourceNotFoundException e) {
            return globalExceptionHandler.handleResourceNotFound(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"id\": 1, \"name\": \"Juan Pérez\", \"email\": \"juan.perez@example.com\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o incompletos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Datos inválidos o incompletos.\"}")
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
    @RequiresPermission("create_users")
    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(201).body(UserMapper.toDto(createdUser));
        } catch (UnauthorizedException e) {
            return globalExceptionHandler.handleUnauthorized(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Actualizar un usuario",
            description = "Actualiza la información de un usuario existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"id\": 1, \"name\": \"Juan Pérez\", \"email\": \"juan.perez@example.com\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado con el ID especificado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuario no encontrado con el ID especificado.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o incompletos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Datos inválidos o incompletos.\"}")
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
    @RequiresPermission("create_users")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toEntity(userDTO);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(UserMapper.toDto(updatedUser));
        } catch (UnauthorizedException e) {
            return globalExceptionHandler.handleUnauthorized(e);
        } catch (ResourceNotFoundException e) {
            return globalExceptionHandler.handleResourceNotFound(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

    @Operation(
            summary = "Eliminar un usuario",
            description = "Elimina un usuario del sistema por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario eliminado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Usuario eliminado correctamente\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado con el ID especificado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuario no encontrado con el ID especificado.\"}")
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
    @RequiresPermission("create_users")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                throw new ResourceNotFoundException("Usuario no encontrado con ID " + id);
            }

            userService.deleteUserById(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (UnauthorizedException e) {
            return globalExceptionHandler.handleUnauthorized(e);
        } catch (ResourceNotFoundException e) {
            return globalExceptionHandler.handleResourceNotFound(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }

}
