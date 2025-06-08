package com.tfg.service.impl;

import com.tfg.dto.RoleDTO;
import com.tfg.entity.Role;
import com.tfg.mapper.RoleMapper;
import com.tfg.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void testGetAllRoles() {
        // Datos simulados
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("User");

        List<Role> mockRoles = Arrays.asList(role1, role2);

        when(roleRepository.findAll()).thenReturn(mockRoles);

        // Ejecución del método
        List<RoleDTO> result = roleService.getAllRoles();

        // Verificación
        assertEquals(2, result.size());
        assertEquals("Admin", result.get(0).getName());
        assertEquals("User", result.get(1).getName());
    }

    @Test
    void testGetRoleById() {
        // Datos simulados
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Ejecución del método
        RoleDTO result = roleService.getRoleById(1L);

        // Verificación
        assertEquals("Admin", result.getName());
    }

    @Test
    void testCreateRole() {
        // Datos simulados
        Role role = new Role();
        role.setName("Admin");

        when(roleRepository.save(role)).thenReturn(role);

        // Ejecución del método
        RoleDTO result = roleService.createRole(role);

        // Verificación
        assertEquals("Admin", result.getName());
    }

    @Test
    void testUpdateRole() {
        // Datos simulados
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin Updated");

        when(roleRepository.save(role)).thenReturn(role);

        // Ejecución del método
        RoleDTO result = roleService.updateRole(role);

        // Verificación
        assertEquals("Admin Updated", result.getName());
    }

    @Test
    void testDeleteRoleById() {
        // Configuración del mock
        Role role = new Role();
        role.setId(1L);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Ejecución del método
        roleService.deleteRoleById(1L);

        // Verificación
        verify(roleRepository, times(1)).delete(role);
    }
}