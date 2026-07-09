package com.example.middleware.feature.processing.application.usecase;
import com.example.middleware.feature.orchestration.application.StageResult;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.processing.application.port.MappingPort;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

import org.springframework.stereotype.Service;

@Service
public class DefaultMappingUseCase implements MappingUseCase {

    private final MappingPort mappingPort;

    public DefaultMappingUseCase(MappingPort mappingPort) {
        this.mappingPort = mappingPort;
    }

    @Override
    public StageResult map(PipelineContext context) {

        RawEvent event = context.getRawEvent();

        MappingContext mappingContext =
                context.getMappingContext();

        TransformedEvent transformed =
                mappingPort.transform(
                        event,
                        mappingContext
                );

        context.setTransformedEvent(transformed);

        return StageResult.SUCCESS;
    }

}