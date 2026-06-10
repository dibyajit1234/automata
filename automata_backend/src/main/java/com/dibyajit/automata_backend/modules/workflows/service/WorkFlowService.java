package com.dibyajit.automata_backend.modules.workflows.service;

import com.dibyajit.automata_backend.modules.workflows.dto.AddStepRequest;
import com.dibyajit.automata_backend.modules.workflows.dto.CreateWorkflowRequest;
import com.dibyajit.automata_backend.modules.workflows.dto.StepResponse;
import com.dibyajit.automata_backend.modules.workflows.dto.WorkFlowResponse;
import com.dibyajit.automata_backend.modules.workflows.entity.Workflow;
import com.dibyajit.automata_backend.modules.workflows.entity.WorkflowSteps;
import com.dibyajit.automata_backend.modules.workflows.repository.WorkFlowRepository;
import com.dibyajit.automata_backend.modules.workflows.repository.WorkFlowStepsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkFlowService {
    private final WorkFlowRepository workFlowRepository;
    private final WorkFlowStepsRepository workFlowStepsRepository;

    public WorkFlowResponse createWorkflow(CreateWorkflowRequest workflowRequest){
        Workflow workflow = new Workflow();
        workflow.setName(workflowRequest.getName());
        workflow.setActive(false);
        Workflow savedWorkflow = workFlowRepository.save(workflow);
        return mapToResponse(savedWorkflow);
    }

    public List<WorkFlowResponse> getAllWorkFlows(){
        return workFlowRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public WorkFlowResponse mapToResponse(Workflow workflow){
        WorkFlowResponse response = new WorkFlowResponse();
        response.setId(workflow.getId());
        response.setActive(workflow.isActive());
        response.setName(workflow.getName());
        return response;
    }

    public StepResponse addStepToWorkflow(UUID workflowid, AddStepRequest stepRequest){
        Workflow workflow = workFlowRepository.findById(workflowid)
                .orElseThrow(()->new IllegalArgumentException("workflow now found"));

        int nextOrderIndex = workFlowStepsRepository.findByWorkflow_IdOrderByOrderIndexAsc(workflowid).size();

        WorkflowSteps steps = new WorkflowSteps();
        steps.setWorkflow(workflow);
        steps.setStepType(stepRequest.getStepType());
        steps.setData(stepRequest.getData());
        steps.setStepType(stepRequest.getStepType());
        steps.setApp(stepRequest.getApp());
        steps.setOrderIndex(nextOrderIndex);
        WorkflowSteps finalStep = workFlowStepsRepository.save(steps);
        return mapToStep(finalStep);
    }
    public  StepResponse mapToStep(WorkflowSteps steps){
        StepResponse step = new StepResponse();
        step.setId(steps.getId());
        step.setStepType(steps.getStepType());
        step.setApp(steps.getApp());
        step.setData(steps.getData());
        step.setOrderIndex(steps.getOrderIndex());
        return step;
    }
}
