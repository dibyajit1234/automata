package com.dibyajit.automata_backend.modules.executions.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@Slf4j
public class ActionExecutorService {
    private final RestClient restClient = RestClient.create();

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000,multiplier = 2)
    )
    public void execute(String app, Map<String,Object> resolvedData){
        log.info("initializing http post to external App: {}",app);
            String targetUrl = (String)resolvedData.get("url");
            if(targetUrl==null || targetUrl.isBlank()) {
                log.error("target url not found exiting");
                return;
            }

            Map<String, String> headers = (Map<String,String>)resolvedData.get("headers");
            var request = restClient.post()
                    .uri(targetUrl)
                    .contentType(MediaType.APPLICATION_JSON);
            if(headers!=null)headers.forEach(request::header);


            String response = request.body(resolvedData)
                    .retrieve()
                    .body(String.class);
            log.info("successfullt sent data to {} response {}",app,response);
    }
    @Recover
    public void recoverFromFailuer(Exception e,String app, Map<String,Object> resolvedData){
        log.error("FATAL: All 3 attempts failed for {}. Error: {}", app, e.getMessage());
        log.warn("Saving failed payload to Dead Letter Queue for manual review: {}", resolvedData);
    }
}
