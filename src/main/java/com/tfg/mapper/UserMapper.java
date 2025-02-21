package com.tfg.mapper;

import com.tfg.dto.UserDTO;
import com.tfg.entity.User;

public class UserMapper {

    public static UserDTO toDto(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPasswordHash(null);
        dto.setEmail(user.getEmail());
        dto.setRole(RoleMapper.toDto(user.getRole()));
        dto.setWarehouse(WarehouseMapper.toDto(user.getWarehouse()));
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(dto.getPasswordHash());
        user.setEmail(dto.getEmail());
        user.setRole(RoleMapper.toEntity(dto.getRole()));
        user.setWarehouse(WarehouseMapper.toEntity(dto.getWarehouse()));
        return user;
    }
}
