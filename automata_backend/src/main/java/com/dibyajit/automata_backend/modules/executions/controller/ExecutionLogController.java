package com.dibyajit.automata_backend.modules.executions.controller;

import com.dibyajit.automata_backend.modules.executions.entity.ExecutionLog;
import com.dibyajit.automata_backend.modules.executions.repository.ExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class ExecutionLogController {
    private final ExecutionLogRepository executionLogRepository;

    @GetMapping("/{workflowId}/logs")
    public ResponseEntity<List<ExecutionLog>> getWorkflowLogs(@PathVariable UUID workflowId){
        List<ExecutionLog> logs = executionLogRepository.findByWorkflowIdOrderByStartedAt(workflowId);
        return ResponseEntity.ok(logs);
    }
}
