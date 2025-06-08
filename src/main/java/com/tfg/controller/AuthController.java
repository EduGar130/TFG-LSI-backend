package com.tfg.controller;

import com.tfg.security.jwt.JwtUtils;
import com.tfg.security.model.JwtRequest;
import com.tfg.security.model.JwtResponse;
import com.tfg.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService, UserService userService,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Operation(
        summary = "Autenticar usuario",
        description = "Autentica un usuario con su nombre de usuario y contraseña, y devuelve un token JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa, token JWT generado"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public JwtResponse authenticate(@RequestBody JwtRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        Long warehouseId = userService.getWarehouseIdByUsername(request.getUsername());
        String permisos = userService.getPermissionsByUsername(request.getUsername());
        String token = jwtUtils.generateToken(userDetails.getUsername(), permisos, warehouseId);
        return new JwtResponse(token);
    }
}
