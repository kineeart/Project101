package com.example.middleware.feature.processing.domain.context;

import java.util.HashMap;
import java.util.Map;

import com.example.middleware.feature.metadata.domain.TableRule;

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