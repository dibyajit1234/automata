package com.dibyajit.automata_backend.modules.workflows.dto;

import lombok.Data;

import java.util.Map;
@Data
public class AddStepRequest {
    private String stepType;
    private String app;
    private Map<String,Object> data;
}
