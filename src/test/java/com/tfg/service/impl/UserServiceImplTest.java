package com.tfg.service.impl;

import com.tfg.entity.Role;
import com.tfg.entity.User;
import com.tfg.entity.Warehouse;
import com.tfg.repository.RoleRepository;
import com.tfg.repository.UserRepository;
import com.tfg.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, warehouseRepository, roleRepository, passwordEncoder);
    }

    @Test
    void testGetAllUsers() {
        // Configuración del mock
        User user1 = new User();
        user1.setId(1L);
        user1.setValido(true);

        User user2 = new User();
        user2.setId(2L);
        user2.setValido(true);

        when(userRepository.findByValido(true)).thenReturn(List.of(user1, user2));

        // Ejecución del método
        var result = userService.getAllUsers();

        // Verificación
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserById() {
        // Configuración del mock
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Ejecución del método
        var result = userService.getUserById(1L);

        // Verificación
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreateUser() {
        // Datos simulados
        Role role = new Role();
        role.setId(1L);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);

        User user = new User();
        user.setRole(role);
        user.setWarehouse(warehouse);
        user.setPasswordHash("password");

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Ejecución del método
        var result = userService.createUser(user);

        // Verificación
        assertNotNull(result);
        assertEquals("hashedPassword", result.getPasswordHash());
    }

    @Test
    void testDeleteUserById() {
        // Ejecución del método
        userService.deleteUserById(1L);

        // Verificación
        verify(userRepository, times(1)).setUserInvalidoById(1L);
    }
}