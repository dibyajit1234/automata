package com.dibyajit.automata_backend.modules.executions.repository;

import com.dibyajit.automata_backend.modules.executions.entity.ExecutionRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExecutionRunRepository extends JpaRepository<ExecutionRun, UUID> {
}
