package com.dibyajit.automata_backend.modules.executions.service;

import com.dibyajit.automata_backend.modules.workflows.entity.WorkflowSteps;
import com.dibyajit.automata_backend.modules.workflows.repository.WorkFlowStepsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecutionService {

    private final WorkFlowStepsRepository workFlowStepsRepository;
    private final TemplateEngineService templateEngine;
    private final ActionExecutorService actionExecutor;

    public void runWorkflow(UUID workflowId, Map<String,Object> payload){
        log.info("Starting workflow execution");

        List<WorkflowSteps> steps = workFlowStepsRepository.findByWorkflow_IdOrderByOrderIndexAsc(workflowId);
        if(steps.isEmpty()){
            log.warn("workflow has no steps setup exiting {}",workflowId);
            return;
        }
        Map<String, Object> state = new HashMap<>();
        state.put("trigger", payload);
        log.info("triggering payload received {}",payload);
        for(WorkflowSteps step:steps){
            if(step.getOrderIndex()==0)continue;
            log.info("Executing step: {} -> action app: {}",step.getOrderIndex(),step.getApp());
            executeAction(step,state);
        }
        log.info("workflow execution complete");
    }
    private void executeAction(WorkflowSteps step, Map<String,Object> state){
        Map<String, Object> resolvedData = templateEngine.resolveActionData(step.getData(), state);
        actionExecutor.execute(step.getApp(),resolvedData);
        log.info("Raw Configuration (From DB) : {}", step.getData());
        log.info("Resolved Data (Mapped)      : {}", resolvedData);

    }
}
