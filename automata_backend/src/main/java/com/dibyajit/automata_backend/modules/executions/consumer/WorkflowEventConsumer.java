package com.dibyajit.automata_backend.modules.executions.consumer;

import com.dibyajit.automata_backend.modules.executions.service.ExecutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowEventConsumer {
    private final ObjectMapper objectMapper;
    private final ExecutionService executionService;

    @KafkaListener(topics="workflow-triggers",groupId="automata-workers")
    public void consumeTriggerEvent(String message){
        try{
            log.info("worker picked a new message from kafka ");
            Map<String,Object> eventData = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            UUID workflowId = UUID.fromString((String)eventData.get("workflowId"));
            Map<String,Object> payload = (Map<String,Object>) eventData.get("payload");
            executionService.runWorkflow(workflowId,payload);
        } catch (JsonMappingException e) {
            log.error("Worker failed to parse Kafka message: {}", message, e);
        } catch (JsonProcessingException e) {
            log.error("Unexpected error during workflow execution", e);
        }
    }
}
