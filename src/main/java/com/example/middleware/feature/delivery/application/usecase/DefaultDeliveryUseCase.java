package com.example.middleware.feature.delivery.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.delivery.application.port.DeliveryArtifactRepository;
import com.example.middleware.feature.delivery.application.port.DeliveryPort;
import com.example.middleware.feature.delivery.domain.DeliveryArtifact;
import com.example.middleware.feature.delivery.domain.DeliveryResult;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.processing.application.port.RetryPort;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
// Bổ sung import interface DeliveryUseCase của bạn tại đây nếu nó nằm ở package khác
// import com.example.middleware.feature.delivery.application.usecase.DeliveryUseCase;

@Service
public class DefaultDeliveryUseCase implements DeliveryUseCase {

    private final DeliveryPort deliveryPort;
    private final RetryPort retryPort;
    private final DeliveryArtifactRepository artifactRepository;    
   public DefaultDeliveryUseCase(
        DeliveryPort deliveryPort,
        RetryPort retryPort,
        DeliveryArtifactRepository artifactRepository) {

    this.deliveryPort = deliveryPort;
    this.retryPort = retryPort;
    this.artifactRepository = artifactRepository;
}

    @Override
    public StageResult deliver(PipelineContext context) {

        TransformedEvent transformed = context.getTransformedEvent();
        String eventId = context.getRawEvent().getEventId();

DeliveryResult deliveryResult;

try {

    deliveryResult =
            retryPort.execute(
                    eventId,
                    transformed.getPayload(),
                    () -> deliveryPort.write(
                            transformed,
                            context
                                    .getMappingContext()
                                    .getDeliveryProfile()
                    )
            );


DeliveryArtifact artifact =
        new DeliveryArtifact(eventId);

artifact.markFailed();

artifactRepository.save(artifact);


    artifact.markPublished();

    artifactRepository.save(
            artifact
    );


    context.setFilePath(
            deliveryResult.fileName()
    );


} catch (Exception ex) {


DeliveryArtifact artifact =
        new DeliveryArtifact(eventId);

artifact.markFailed();

artifactRepository.save(artifact);


    artifact.markFailed();


    artifactRepository.save(
            artifact
    );


    throw ex;
}
        return StageResult.SUCCESS;
        
    }
}
