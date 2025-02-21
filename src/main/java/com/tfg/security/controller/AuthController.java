package com.tfg.controller;

import com.tfg.security.jwt.JwtUtils;
import com.tfg.security.model.JwtRequest;
import com.tfg.security.model.JwtResponse;
import com.tfg.service.UserService;
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
