package com.tfg.mapper;

import com.tfg.dto.RoleDTO;
import com.tfg.entity.Role;

public class RoleMapper {

    public static RoleDTO toDto(Role role) {
        if (role == null) return null;
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setPermissions(role.getPermissions());
        dto.setIsGlobal(role.getIsGlobal());
        return dto;
    }

    public static Role toEntity(RoleDTO dto) {
        if(dto == null) return null;
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setPermissions(dto.getPermissions());
        role.setIsGlobal(dto.getIsGlobal());
        return role;
    }
}
