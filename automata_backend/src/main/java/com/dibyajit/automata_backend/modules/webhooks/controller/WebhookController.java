package com.dibyajit.automata_backend.modules.webhooks.controller;

import com.dibyajit.automata_backend.modules.webhooks.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhooks")
@Slf4j
public class WebhookController {
        private final WebhookService webhookService;

        @PostMapping("/catch/{workflowId}")
        public ResponseEntity<String> catchWebhook(@PathVariable UUID workflowId, @RequestBody Map<String,Object> payload){
            log.info("Received webhook for workflow: {}", workflowId);
            webhookService.processIncomingWebhook(workflowId,payload);
            return ResponseEntity.accepted().body("webhook received and queued");
        }
}
