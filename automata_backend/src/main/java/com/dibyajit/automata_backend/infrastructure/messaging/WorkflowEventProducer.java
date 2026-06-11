package com.dibyajit.automata_backend.infrastructure.messaging;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class WorkflowEventProducer {
    private final KafkaTemplate<String,String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private static final String TOPIC = "workflow-triggers";

    public WorkflowEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public  void pushTriggeredEvents(UUID workflowId, Map<String,Object> payload){
        try {
            Map<String,Object> event = new HashMap<>();
            event.put("workflowId",workflowId);
            event.put("payload",payload);
            event.put("timestamp",System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC,workflowId.toString(),message);
            log.info("Published trigger event for workflow: {}",workflowId);
        } catch (Exception e){
            log.error("Failed to serialize webhook payload for workflow {}", workflowId, e);
            throw new RuntimeException("Error processing webhook data");
        }
    }
}
