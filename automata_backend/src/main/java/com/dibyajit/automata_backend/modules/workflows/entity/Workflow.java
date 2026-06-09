package com.dibyajit.automata_backend.modules.workflows.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workflows")
@Data
public class Workflow {
    @Id
    @Column(name = "id",updatable = false,nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private boolean isActive=false;

    @OneToMany(mappedBy = "workflow",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkflowSteps> workFlowSteps;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt=LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();


}
