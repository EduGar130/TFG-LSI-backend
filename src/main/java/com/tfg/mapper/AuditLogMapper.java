package com.tfg.mapper;

import com.tfg.dto.AuditLogDTO;
import com.tfg.entity.AuditLog;

public class AuditLogMapper {

    public static AuditLogDTO toDto(AuditLog log) {
        if (log == null) return null;
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(log.getId());
        dto.setAction(log.getAction());
        dto.setTableName(log.getTableName());
        dto.setRecordId(log.getRecordId());
        dto.setUser(UserMapper.toDto(log.getUser()));
        dto.setDetails(log.getDetails());
        dto.setCreatedAt(log.getCreatedAt());
        return dto;
    }
}
