package com.example.middleware.feature.metadata.infrastructure.config;

import java.util.ArrayList;
import java.util.List;

public class TableRuleProperties {

    private String sourceTable;

    private String targetTable;

    private List<FieldRuleProperties> fields =
            new ArrayList<>();

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public List<FieldRuleProperties> getFields() {
        return fields;
    }

    public void setFields(
            List<FieldRuleProperties> fields) {

        this.fields = fields;
    }
}