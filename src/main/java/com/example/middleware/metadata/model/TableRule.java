package com.example.middleware.metadata.model;

import java.util.ArrayList;
import java.util.List;

public class TableRule {

    private String sourceTable;
    private String targetTable;
    private List<FieldRule> fieldRules = new ArrayList<>();

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

    public List<FieldRule> getFieldRules() {
        return fieldRules;
    }

    public void setFieldRules(List<FieldRule> fieldRules) {
        this.fieldRules = fieldRules;
    }
}