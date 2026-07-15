package com.example.middleware.feature.metadata.infrastructure.config;
import java.util.Map;
public class FieldRuleProperties {
private Map<String, String> xrefDictionary;
    private String sourceField;

    private String targetField;

    private String dataType;

    private boolean required;

    private String defaultValue;

    public String getSourceField() {
        return sourceField;
    }
public Map<String, String> getXrefDictionary() {
    return xrefDictionary;
}

public void setXrefDictionary(Map<String, String> xrefDictionary) {
    this.xrefDictionary = xrefDictionary;
}
    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
}