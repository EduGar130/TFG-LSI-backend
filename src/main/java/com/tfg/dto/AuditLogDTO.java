package com.tfg.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private Long id;
    private String action;
    private String tableName;
    private Integer recordId;
    private UserDTO user;
    private String details;
    private LocalDateTime createdAt;
}
