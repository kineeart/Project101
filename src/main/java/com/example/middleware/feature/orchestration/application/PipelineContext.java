package com.example.middleware.feature.orchestration.application;

import com.example.middleware.feature.orchestration.domain.Execution;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

import java.util.HashMap;
import java.util.Map;

public class PipelineContext {
    private String filePath;
    private RawEvent rawEvent;

    private TransformedEvent transformedEvent;

    private MappingContext mappingContext;

    private Execution execution;

    private final Map<String, Object> attributes = new HashMap<>();

 public PipelineContext() {
}

public PipelineContext(
        RawEvent rawEvent,
        TransformedEvent transformedEvent,
        MappingContext mappingContext,
        Execution execution) {

    this.rawEvent = rawEvent;
    this.transformedEvent = transformedEvent;
    this.mappingContext = mappingContext;
    this.execution = execution;
}

    public RawEvent getRawEvent() {
        return rawEvent;
    }

    public void setRawEvent(RawEvent rawEvent) {
        this.rawEvent = rawEvent;
    }

    public TransformedEvent getTransformedEvent() {
        return transformedEvent;
    }

    public void setTransformedEvent(TransformedEvent transformedEvent) {
        this.transformedEvent = transformedEvent;
    }

    public MappingContext getMappingContext() {
        return mappingContext;
    }

    public void setMappingContext(MappingContext mappingContext) {
        this.mappingContext = mappingContext;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    // Generic Attribute Store

    public <T> void put(String key, T value) {
        attributes.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(attributes.get(key));
    }

    // Helpers

    public boolean hasTransformedEvent() {
        return transformedEvent != null;
    }

    public boolean hasExecution() {
        return execution != null;
    }

public String getFilePath() {
    return filePath;
}

public void setFilePath(String filePath) {
    this.filePath = filePath;
}
}