package com.tfg.service;

import com.tfg.entity.AuditLog;

import java.util.List;

public interface AuditLogService {
    List<AuditLog> getAllLogs();
}
