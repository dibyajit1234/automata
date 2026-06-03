package com.dibyajit.automata_backend.modules.workflows.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "workflow_steps")
@Data

public class WorkflowSteps {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false,nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id",nullable = false)
    private Workflow workflow;

    @Column(nullable = false)
    private String stepType;

    @Column(nullable = false)
    private int orderIndex;

    @Column(nullable = false)
    private String app;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false,columnDefinition = "jsonb")
    private Map<String, Object> data;

}
