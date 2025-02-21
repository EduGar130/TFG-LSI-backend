package com.tfg.service;

import com.tfg.dto.RoleDTO;
import com.tfg.entity.Role;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRoles();

    RoleDTO getRoleById(Long id);

    RoleDTO createRole(Role role);

    RoleDTO updateRole(Role role);

    void deleteRoleById(Long id);
}
