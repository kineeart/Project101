package com.example.middleware.feature.orchestration.application.builder;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;

public interface PipelineContextBuilder {

    PipelineContext build(
        RawEvent event
);

}