package com.example.middleware.feature.orchestration.application.builder;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.domain.Execution;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import org.springframework.stereotype.Service;

@Service
public class DefaultPipelineContextBuilder
        implements PipelineContextBuilder {

    @Override
    public PipelineContext build(
            RawEvent event,
            MappingContext mappingContext,
            Execution execution) {

        PipelineContext context =
                new PipelineContext();

        context.setRawEvent(event);
        context.setMappingContext(mappingContext);
        context.setExecution(execution);

        return context;
    }
}