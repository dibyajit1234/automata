package com.dibyajit.automata_backend.modules.executions.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateEngineService {
    private final ObjectMapper objectMapper;

    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("\\{\\{(.+?)\\}\\}");
    public Map<String, Object> resolveActionData(Map<String,Object> stepConfig,Map<String,Object> state){
        JsonNode stateTree = objectMapper.valueToTree(state);
        Map<String,Object> resolveConfig = new HashMap<>();

        for(Map.Entry<String,Object> entry: stepConfig.entrySet()){
            Object value = entry.getValue();
            if (value instanceof String stringValue) {
                String resolvedString = resolveString(stringValue, stateTree);
                resolveConfig.put(entry.getKey(), resolvedString);
        }else {
                resolveConfig.put(entry.getKey(), value);
            }
        }
        return resolveConfig;

    }
    private String resolveString(String template, JsonNode stateTree){
        Matcher matcher = TEMPLATE_PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            // e.g., extracts "trigger.issue.title"
            String path = matcher.group(1).trim();

            // Jackson JsonPointer uses slashes for paths: /trigger/issue/title
            String jsonPointer = "/" + path.replace(".", "/");

            // Look up the value in the JSON tree
            JsonNode targetNode = stateTree.at(jsonPointer);

            // Replace the {{...}} with the actual value. If it doesn't exist, replace with empty string.
            String replacement = (targetNode.isMissingNode() || targetNode.isNull()) ? "" : targetNode.asText();

            // Matcher.quoteReplacement prevents errors if the replaced text contains special characters like '$'
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
