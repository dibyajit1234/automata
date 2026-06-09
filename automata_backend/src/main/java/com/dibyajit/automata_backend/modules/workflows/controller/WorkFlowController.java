package com.dibyajit.automata_backend.modules.workflows.controller;

import com.dibyajit.automata_backend.modules.workflows.dto.CreateWorkflowRequest;
import com.dibyajit.automata_backend.modules.workflows.dto.WorkFlowResponse;
import com.dibyajit.automata_backend.modules.workflows.service.WorkFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkFlowController {
    private final WorkFlowService workFlowService;

    @PostMapping
    public ResponseEntity<WorkFlowResponse> createWorkflow(@RequestBody CreateWorkflowRequest request){
        WorkFlowResponse response =  workFlowService.createWorkflow(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkFlowResponse>> getAllWorkflow(){
        List<WorkFlowResponse>responses = workFlowService.getAllWorkFlows();
        return ResponseEntity.ok(responses);
    }
}
