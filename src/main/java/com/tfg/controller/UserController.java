package com.tfg.controller;

import com.tfg.dto.UserDTO;
import com.tfg.entity.User;
import com.tfg.exception.ResourceNotFoundException;
import com.tfg.mapper.UserMapper;
import com.tfg.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Listar todos los usuarios",
            description = "Devuelve una lista con todos los usuarios registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Obtener un usuario por ID",
            description = "Devuelve los datos del usuario correspondiente al ID proporcionado, si existe."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID " + id);
        }
        return UserMapper.toDto(user);
    }

    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/add")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        User createdUser = userService.createUser(user);
        return UserMapper.toDto(createdUser);
    }

    @Operation(
            summary = "Actualizar un usuario",
            description = "Actualiza la información de un usuario existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/update")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        User updatedUser = userService.updateUser(user);
        return UserMapper.toDto(updatedUser);
    }

    @Operation(
            summary = "Eliminar un usuario",
            description = "Elimina un usuario del sistema por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "401", description = "No autorizado – token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

}
