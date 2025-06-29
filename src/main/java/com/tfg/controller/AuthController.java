package com.tfg.controller;

import com.tfg.exception.GlobalExceptionHandler;
import com.tfg.security.jwt.JwtUtils;
import com.tfg.security.model.JwtRequest;
import com.tfg.security.model.JwtResponse;
import com.tfg.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import com.tfg.security.service.CustomUserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final GlobalExceptionHandler globalExceptionHandler;

    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService, UserService userService,
                          JwtUtils jwtUtils, GlobalExceptionHandler globalExceptionHandler) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Operation(
        summary = "Autenticar usuario",
        description = "Autentica un usuario con su nombre de usuario y contraseña, y devuelve un token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa, token JWT generado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c3VhcmlvIiwicGVybWlzb3MiOiJmdWxsX2FjY2VzcyIsIndhcmVob3VzZUlkIjoxLCJleHAiOjE2NzAwMDAwMDB9.abc123xyz456\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"timestamp\": \"2023-10-01T12:00:00\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Credenciales inválidas.\"}")
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
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            if (!userService.getUserByUsername(request.getUsername()).getValido()) {
                throw new BadCredentialsException("Usuario no encontrado");
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            Long warehouseId = userService.getWarehouseIdByUsername(request.getUsername());
            String permisos = userService.getPermissionsByUsername(request.getUsername());
            String token = jwtUtils.generateToken(userDetails.getUsername(), permisos, warehouseId);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            return globalExceptionHandler.handleBadCredentials(e);
        } catch (Exception e) {
            return globalExceptionHandler.handleGenericException(e);
        }
    }
}
