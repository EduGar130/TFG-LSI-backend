package com.tfg.service;

import com.tfg.entity.AuditLog;

import java.util.List;

public interface AuditLogService {
    List<AuditLog> getAllLogs();

    /**
     * Devuelve los logs filtrando por acción.
     *
     * @param action tipo de acción realizada
     * @return lista de logs correspondientes
     */
    List<AuditLog> getLogsByAction(String action);
}
