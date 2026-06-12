package com.dibyajit.automata_backend.modules.executions.service;

import com.dibyajit.automata_backend.modules.workflows.entity.WorkflowSteps;
import com.dibyajit.automata_backend.modules.workflows.repository.WorkFlowStepsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecutionService {

    private final WorkFlowStepsRepository workFlowStepsRepository;


    public void runWorkflow(UUID workflowId, Map<String,Object> payload){
        log.info("Starting workflow execution");

        List<WorkflowSteps> steps = workFlowStepsRepository.findByWorkflow_IdOrderByOrderIndexAsc(workflowId);
        if(steps.isEmpty()){
            log.warn("workflow has no steps setup exiting {}",workflowId);
            return;
        }
        log.info("triggering payload received {}",payload);
        for(WorkflowSteps step:steps){
            if(step.getOrderIndex()==0)continue;
            log.info("Executing step: {} -> action app: {}",step.getOrderIndex(),step.getApp());
            executeAction(step,payload);
        }
        log.info("workflow execution complete");
    }
    private void executeAction(WorkflowSteps step, Map<String,Object> payload){
        Map<String,Object> config = step.getData();
        log.info("Simulated API Call to {} ", step.getApp());
        log.info("Configuration: {}", config);

    }
}
