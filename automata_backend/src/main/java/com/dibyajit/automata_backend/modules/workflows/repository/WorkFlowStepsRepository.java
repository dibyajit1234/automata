package com.dibyajit.automata_backend.modules.workflows.repository;

import com.dibyajit.automata_backend.modules.workflows.entity.WorkflowSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkFlowStepsRepository extends JpaRepository<WorkflowSteps, UUID> {
    List<WorkflowSteps> findByWorkflow_IdOrderByOrderIndexAsc(UUID workflosId);
}
