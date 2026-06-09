package com.dibyajit.automata_backend.modules.workflows.service;

import com.dibyajit.automata_backend.modules.workflows.dto.CreateWorkflowRequest;
import com.dibyajit.automata_backend.modules.workflows.dto.WorkFlowResponse;
import com.dibyajit.automata_backend.modules.workflows.entity.Workflow;
import com.dibyajit.automata_backend.modules.workflows.repository.WorkFlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkFlowService {
    private final WorkFlowRepository workFlowRepository;

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
}
