package com.dibyajit.automata_backend.modules.executions.service;

import com.dibyajit.automata_backend.modules.executions.entity.ExecutionLog;
import com.dibyajit.automata_backend.modules.executions.repository.ExecutionLogRepository;
import com.dibyajit.automata_backend.modules.workflows.entity.WorkflowSteps;
import com.dibyajit.automata_backend.modules.workflows.repository.WorkFlowStepsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final FilterService filterService;
    private final ExecutionLogRepository executionLogRepository;

    public void runWorkflow(UUID workflowId, Map<String,Object> payload){
        log.info("Starting workflow execution");


        ExecutionLog executionLog = ExecutionLog.builder()
                .workflowId(workflowId)
                .status("RUNNING")
                .startedAt(LocalDateTime.now())
                .triggerPayload(payload)
                .build();

        try{
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
                if("filter".equalsIgnoreCase(step.getStepType())){
                    log.info("evaluating step filter : {}",step.getOrderIndex());
                    boolean passed = filterService.evaluate(step.getData(),state);
                    if(!passed){
                        log.warn("filter condition not met :{}, halting the execution",step.getOrderIndex());
                        executionLog.setStatus("HALTED_BY_FILTER");
                        executionLog.setErrorMessage("filter condition not met :"+step.getOrderIndex()+", halting the execution");
                        return;
                    }
                    log.info("filter condition passed, moving on to next step ...");

                    continue;
                }
                log.info("Executing step: {} -> action app: {}",step.getOrderIndex(),step.getApp());
                executeAction(step,state);
            }
            executionLog.setStatus("SUCCESS");
        }catch (Exception e){
            log.error("workflow execution crashed:{}",e.getMessage(),e);
            executionLog.setStatus("FAILED");
            executionLog.setErrorMessage(e.getMessage());
        }finally {
            executionLog.setCompletedAt(LocalDateTime.now());
            executionLogRepository.save(executionLog);
            log.info("workflow execution complete and logged");
        }

    }
    private void executeAction(WorkflowSteps step, Map<String,Object> state){
        Map<String, Object> resolvedData = templateEngine.resolveActionData(step.getData(), state);
        actionExecutor.execute(step.getApp(),resolvedData);
        log.info("Raw Configuration (From DB) : {}", step.getData());
        log.info("Resolved Data (Mapped)      : {}", resolvedData);

    }
}
