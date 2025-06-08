package com.tfg.service.impl;

import com.tfg.entity.AuditLog;
import com.tfg.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuditLogServiceImplTest {

    private AuditLogServiceImpl auditLogService;

    @Mock
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auditLogService = new AuditLogServiceImpl(auditLogRepository);
    }

    @Test
    void testGetAllLogs() {
        AuditLog log1 = new AuditLog();
        log1.setId(1L);
        log1.setAction("CREATE");

        AuditLog log2 = new AuditLog();
        log2.setId(2L);
        log2.setAction("DELETE");

        List<AuditLog> logs = Arrays.asList(log1, log2);
        when(auditLogRepository.findAll()).thenReturn(logs);

        List<AuditLog> result = auditLogService.getAllLogs();

        assertEquals(2, result.size());
        assertEquals("CREATE", result.get(0).getAction());
    }

    @Test
    void testGetLogsByAction() {
        AuditLog log1 = new AuditLog();
        log1.setId(1L);
        log1.setAction("CREATE");

        when(auditLogRepository.findByAction("CREATE")).thenReturn(List.of(log1));

        List<AuditLog> result = auditLogService.getLogsByAction("CREATE");

        assertEquals(1, result.size());
        assertEquals("CREATE", result.get(0).getAction());
    }
}

