package com.dibyajit.automata_backend.modules.webhooks.service;

import com.dibyajit.automata_backend.infrastructure.messaging.WorkflowEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebhookService {
    private final WorkflowEventProducer workflowEventProducer;

    public void processIncomingWebhook(UUID workflowId, Map<String,Object> payload){
        workflowEventProducer.pushTriggeredEvents(workflowId,payload);
    }
}
