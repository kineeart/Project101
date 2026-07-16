package com.example.middleware.feature.processing.application.usecase;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.ingestion.application.RequestValidationService;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.processing.application.port.IdempotencyPort;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.exception.DuplicateEventException;

import org.springframework.stereotype.Service;
@Service
public class DefaultValidationUseCase implements ValidationUseCase {
    private final RequestValidationService requestValidationService;

private final IdempotencyPort idempotencyPort;
public DefaultValidationUseCase(
        RequestValidationService requestValidationService,
        IdempotencyPort idempotencyPort) {

    this.requestValidationService = requestValidationService;
    this.idempotencyPort = idempotencyPort;
}
@Override
public StageResult validate(PipelineContext context) {

    RawEvent event = context.getRawEvent();

    requestValidationService.validate(
            event.getPayload()
    );

    if (!idempotencyPort.isNewEvent(
            event.getEventId())) {

        throw new DuplicateEventException(event.getEventId());
    }

    return StageResult.SUCCESS;
}

}
