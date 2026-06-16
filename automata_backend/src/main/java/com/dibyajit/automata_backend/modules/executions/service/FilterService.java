package com.dibyajit.automata_backend.modules.executions.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterService {
    private final TemplateEngineService templateEngine;

    public boolean evaluate(Map<String, Object> rawFilterData, Map<String, Object> state) {
        Map<String, Object> resolvedData = templateEngine.resolveActionData(rawFilterData, state);
        //resolve templates frist
        String left = String.valueOf(resolvedData.get("left_operand")).trim();
        String right = String.valueOf(resolvedData.get("right_operand")).trim();
        String condition = String.valueOf(resolvedData.get("condition")).toLowerCase();

        log.info("Evaluating Filter: [{}] {} [{}]", left, condition, right);

        //Evaluate the logic bsaed on the condition
        return switch (condition) {
            case "equals", "==" -> left.equalsIgnoreCase(right);
            case "not_equals", "!=" -> !left.equalsIgnoreCase(right);
            case "contains" -> left.toLowerCase().contains(right.toLowerCase());
            default -> {
                log.warn("Unknown filter condition: '{}'. Defaulting to false.", condition);
                yield false;
            }
        };
    }
}
