package com.tfg.service.impl;

import com.tfg.entity.AuditLog;
import com.tfg.repository.AuditLogRepository;
import com.tfg.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    public List<AuditLog> getLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }
}
