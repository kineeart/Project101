package com.example.middleware.feature.processing.domain.context;

import com.example.middleware.metadata.model.TableRule;

import java.util.HashMap;
import java.util.Map;

public class MappingContext {

    private final Map<String, TableRule> tableRules = new HashMap<>();

    public void addRule(String sourceTable,
                        TableRule tableRule) {
        tableRules.put(sourceTable, tableRule);
    }

    public TableRule getRule(String sourceTable) {
        return tableRules.get(sourceTable);
    }
}