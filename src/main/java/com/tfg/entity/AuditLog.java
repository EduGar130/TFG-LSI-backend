package com.tfg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Integer recordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "jsonb")
    private String details;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AuditLog(String insert, String products, int recordId, User user, String details) {
        this.action = insert;
        this.tableName = products;
        this.recordId = recordId;
        this.user = user;
        this.details = details;
    }
}
