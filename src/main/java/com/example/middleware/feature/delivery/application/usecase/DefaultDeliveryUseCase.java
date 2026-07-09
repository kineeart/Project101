package com.example.middleware.feature.delivery.application.usecase;

import com.example.middleware.feature.delivery.application.port.DeliveryPort;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.processing.application.port.RetryPort;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import org.springframework.stereotype.Service;

@Service
public class DefaultDeliveryUseCase implements DeliveryUseCase {

    private final DeliveryPort deliveryPort;
    private final RetryPort retryPort;

    public DefaultDeliveryUseCase(
            DeliveryPort deliveryPort,
            RetryPort retryPort) {

        this.deliveryPort = deliveryPort;
        this.retryPort = retryPort;
    }

    @Override
    public StageResult deliver(PipelineContext context) {

        TransformedEvent transformed =
                context.getTransformedEvent();

        String eventId =
                context.getRawEvent().getEventId();

        String filePath =
                retryPort.execute(
                        eventId,
                        transformed.getPayload(),
                        () -> deliveryPort.write(transformed)
                );

        context.setFilePath(filePath);

        return StageResult.SUCCESS;
    }
}