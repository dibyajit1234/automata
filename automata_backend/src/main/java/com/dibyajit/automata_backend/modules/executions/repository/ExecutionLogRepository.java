package com.dibyajit.automata_backend.modules.executions.repository;

import com.dibyajit.automata_backend.modules.executions.entity.ExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, UUID> {


}
