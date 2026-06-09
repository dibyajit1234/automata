package com.dibyajit.automata_backend.modules.workflows.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class WorkFlowResponse {
    private UUID id;
    private String name;
    private boolean isActive;
}
