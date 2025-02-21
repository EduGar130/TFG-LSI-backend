package com.tfg.dto;

import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    private String name;
    private String permissions;
    private Boolean isGlobal;
}
