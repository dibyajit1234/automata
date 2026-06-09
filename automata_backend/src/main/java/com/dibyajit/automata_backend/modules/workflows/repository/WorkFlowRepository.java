package com.dibyajit.automata_backend.modules.workflows.repository;

import com.dibyajit.automata_backend.modules.workflows.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkFlowRepository extends JpaRepository<Workflow, UUID> {
}
