package com.dibyajit.automata_backend.modules.workflows.repository;

import com.dibyajit.automata_backend.modules.workflows.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface WorkFlowRepository extends JpaRepository<Workflow, UUID> {
}
