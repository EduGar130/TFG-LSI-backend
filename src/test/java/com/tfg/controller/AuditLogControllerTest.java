package com.tfg.controller;

import com.tfg.entity.AuditLog;
import com.tfg.service.AuditLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditLogController.class)
class AuditLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditLogService auditLogService;

    @Test
    void testGetLogsByAction() throws Exception {
        AuditLog log = new AuditLog();
        log.setAction("CREATE");
        when(auditLogService.getLogsByAction("CREATE")).thenReturn(List.of(log));

        mockMvc.perform(get("/api/audit-logs/action/CREATE"))
                .andExpect(status().isOk());
    }
}

