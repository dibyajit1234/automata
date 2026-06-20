package com.dibyajit.automata_backend.modules.executions.service;

import com.dibyajit.automata_backend.modules.workflows.entity.Workflow;
import com.dibyajit.automata_backend.modules.workflows.entity.WorkflowSteps;
import com.dibyajit.automata_backend.modules.workflows.repository.WorkFlowRepository;
import com.dibyajit.automata_backend.modules.workflows.repository.WorkFlowStepsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jdbc.Work;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollingTriggerService {
    private final WorkFlowRepository workFlowRepository;
    private final ExecutionService executionService;

    @Scheduled(fixedRate = 60000)
    public void pollExternalApis(){
        log.info("waking up to check new external events");
        List<Workflow> activeWorkflows = workFlowRepository.findAll()
                .stream()
                .filter(Workflow::isActive)
                .toList();
        if(activeWorkflows.isEmpty()){
            log.info("no active workflows foung");
            return;
        }
        for(Workflow workflow:activeWorkflows){
            Map<String,Object> newEvent = simulateApiCheck(workflow.getId());
            if(newEvent!=null && !newEvent.isEmpty()){
                log.info("found data for workflow {}",workflow.getId());
                executionService.runWorkflow(workflow.getId(),newEvent);
            }
        }
    }

    private Map<String, Object> simulateApiCheck(UUID workflowId) {
        // In reality, you would check a database to see if we already processed this event.
        // For this test, we will use Math.random() to simulate finding a "new" email 30% of the time.

        boolean foundNewData = Math.random() > 0.7;

        if (foundNewData) {
            Map<String, Object> issue = new HashMap<>();
            issue.put("title", "Server out of memory (Polled Event)");
            issue.put("priority", "high");
            issue.put("user", "System_Monitor");

            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "generated");
            payload.put("issue", issue);

            return payload;
        }

        return null; // No new data found
    }
}
