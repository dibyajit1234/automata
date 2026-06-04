package com.dibyajit.automata_backend.modules.executions.entity;

import com.dibyajit.automata_backend.modules.workflows.entity.WorkflowSteps;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "execution_logs")
@Data
public class ExecutionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false)
    private ExecutionRun run;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id")
    private WorkflowSteps step;

    @Column(nullable = false)
    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_data", columnDefinition = "jsonb")
    private Map<String, Object> requestData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_data", columnDefinition = "jsonb")
    private Map<String, Object> responseData;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Column(name = "executed_at", nullable = false, updatable = false)
    private LocalDateTime executedAt = LocalDateTime.now();
}
