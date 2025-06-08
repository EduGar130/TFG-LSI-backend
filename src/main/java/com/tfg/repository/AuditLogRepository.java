package com.tfg.repository;

import com.tfg.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    /**
     * Busca logs de auditoría por tipo de acción.
     *
     * @param action acción realizada (CREATE, UPDATE, DELETE, etc.)
     * @return lista de logs que coinciden con la acción
     */
    List<AuditLog> findByAction(String action);
}
