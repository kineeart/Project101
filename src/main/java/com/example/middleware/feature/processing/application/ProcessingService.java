package com.example.middleware.feature.processing.application;
import com.example.middleware.feature.delivery.application.port.DeliveryPort;
import com.example.middleware.feature.audit.application.port.AuditPort;
import com.example.middleware.feature.ingestion.application.ReceiveEventUseCase;
import com.example.middleware.feature.processing.application.port.RetryPort;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import com.example.middleware.feature.processing.domain.exception.DuplicateEventException;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.domain.Execution;
import com.example.middleware.shared.enums.PipelineStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProcessingService implements ReceiveEventUseCase {
	private final Pipeline pipeline;
    private final MappingContext context;
    private final AuditPort auditPort;
    private final RetryPort retryPort;
	private final DeliveryPort deliveryPort;
	
    public ProcessingService(
	    MappingContext context,
	    AuditPort auditPort,
	    RetryPort retryPort,
	    DeliveryPort deliveryPort,
	Pipeline pipeline) {

	this.context = context;
	this.auditPort = auditPort;
	this.retryPort = retryPort;
	this.deliveryPort = deliveryPort;
	this.pipeline = pipeline;
    }

	@Override
	public ResponseEntity<?> receive(Map<String, Object> request) {

	String eventId = "UNKNOWN";

	try {
	    

	    eventId = request.get("eventId").toString();

	    auditPort.log(eventId, PipelineStatus.RECEIVED,
		    "Webhook received", null);

	    

	    auditPort.log(eventId, PipelineStatus.VALIDATED,
		    "Validation passed", null);

	    RawEvent event = new RawEvent(
		    eventId,
		    "PROFILE_1",
		    "HQ_Price_Master",
		    request
	    );
		Execution execution =
        new Execution(eventId);

PipelineContext pipelineContext =
        new PipelineContext();

pipelineContext.setRawEvent(event);
pipelineContext.setMappingContext(context);
pipelineContext.setExecution(execution);

pipeline.execute(pipelineContext);

TransformedEvent transformed =
        pipelineContext.getTransformedEvent();
	    auditPort.log(eventId, PipelineStatus.TRANSFORMED,
		    "Transformation completed", null);

	    if (transformed.getPayload() == null
		    || transformed.getPayload().isEmpty()) {
		return ResponseEntity.badRequest()
			.body("No data after mapping.");
	    }

	    String filePath = retryPort.execute(
        eventId,
        transformed.getPayload(),
        () -> deliveryPort.write(transformed)
);

	    auditPort.log(eventId, PipelineStatus.WRITTEN,
		    "CSV created", filePath);

	    return ResponseEntity.ok(
		    Map.of(
			    "status", "SUCCESS",
			    "eventId", eventId,
			    "filePath", filePath
		    )
	    );

	} 
	catch (DuplicateEventException e) {

    auditPort.log(
            eventId,
            PipelineStatus.FAILED,
            e.getMessage(),
            null
    );

    return ResponseEntity.status(409)
            .body(e.getMessage());
}
	catch (Exception e) {

	    auditPort.log(eventId, PipelineStatus.FAILED,
		    e.getMessage(), null);

	    auditPort.error(eventId, e);

	    return ResponseEntity.internalServerError()
		    .body(e.getMessage());
	}
    }
}