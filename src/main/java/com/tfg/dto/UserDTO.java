package com.tfg.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
    private RoleDTO role;
    private WarehouseDTO warehouse;
}
