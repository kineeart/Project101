package com.example.middleware.feature.orchestration.application.builder;
import com.example.middleware.feature.orchestration.application.factory.ExecutionFactory;
import com.example.middleware.feature.metadata.application.builder.MappingContextBuilder;
import com.example.middleware.feature.metadata.application.builder.MappingContextBuilder;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.factory.ExecutionFactory;
import com.example.middleware.feature.orchestration.domain.Execution;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import org.springframework.stereotype.Service;

@Service
public class DefaultPipelineContextBuilder
        implements PipelineContextBuilder {
private final ExecutionFactory executionFactory;

private final MappingContextBuilder mappingContextBuilder;
public DefaultPipelineContextBuilder(
        ExecutionFactory executionFactory,
        MappingContextBuilder mappingContextBuilder) {

    this.executionFactory = executionFactory;
    this.mappingContextBuilder = mappingContextBuilder;
}
    @Override
public PipelineContext build(
        RawEvent event) {

  PipelineContext context = new PipelineContext();

context.setRawEvent(event);

context.setMappingContext(
        mappingContextBuilder.build(
                event.getProfileId()
        )
);

context.setExecution(
        executionFactory.create(
                event.getEventId()
        )
);

return context;
    }
}