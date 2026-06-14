package com.dibyajit.automata_backend.modules.workflows.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;
@Data
public class StepResponse {
    private UUID id;
    private String stepType;
    private int orderIndex;
    private String app;
    private Map<String,Object> data;
    private Map<String,Object> headers;
}
