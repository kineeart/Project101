package com.example.middleware.feature.processing.application;

import com.example.middleware.feature.audit.application.port.AuditPort;
import com.example.middleware.feature.delivery.application.port.DeliveryPlugin;
import com.example.middleware.feature.delivery.application.registry.DeliveryPluginRegistry;
import com.example.middleware.feature.ingestion.application.ReceiveEventUseCase;
import com.example.middleware.feature.processing.application.port.IdempotencyPort;
import com.example.middleware.feature.processing.application.port.MappingPort;
import com.example.middleware.feature.processing.application.port.RetryPort;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import com.example.middleware.feature.ingestion.application.RequestValidationService;
import com.example.middleware.shared.enums.PipelineStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProcessingService implements ReceiveEventUseCase {

    private final MappingContext context;
    private final RequestValidationService requestValidationService;
    private final AuditPort auditPort;
    private final IdempotencyPort idempotencyPort;
    private final MappingPort mappingPort;
    private final RetryPort retryPort;
    private final DeliveryPluginRegistry deliveryPluginRegistry;

    public ProcessingService(
	    MappingContext context,
	    RequestValidationService requestValidationService,
	    AuditPort auditPort,
	    IdempotencyPort idempotencyPort,
	    MappingPort mappingPort,
	    RetryPort retryPort,
	    DeliveryPluginRegistry deliveryPluginRegistry) {

	this.context = context;
	this.requestValidationService = requestValidationService;
	this.auditPort = auditPort;
	this.idempotencyPort = idempotencyPort;
	this.mappingPort = mappingPort;
	this.retryPort = retryPort;
	this.deliveryPluginRegistry = deliveryPluginRegistry;
    }

	@Override
	public ResponseEntity<?> receive(Map<String, Object> request) {

	String eventId = "UNKNOWN";

	try {
	    requestValidationService.validate(request);

	    eventId = request.get("eventId").toString();

	    auditPort.log(eventId, PipelineStatus.RECEIVED,
		    "Webhook received", null);

	    if (!idempotencyPort.isNewEvent(eventId)) {
		return ResponseEntity.status(409)
			.body("Duplicate eventId: " + eventId);
	    }

	    auditPort.log(eventId, PipelineStatus.VALIDATED,
		    "Validation passed", null);

	    RawEvent event = new RawEvent(
		    eventId,
		    "PROFILE_1",
		    "HQ_Price_Master",
		    request
	    );

	    TransformedEvent transformed = mappingPort.transform(event, context);

	    auditPort.log(eventId, PipelineStatus.TRANSFORMED,
		    "Transformation completed", null);

	    if (transformed.getPayload() == null
		    || transformed.getPayload().isEmpty()) {
		return ResponseEntity.badRequest()
			.body("No data after mapping.");
	    }

	    DeliveryPlugin plugin = deliveryPluginRegistry.defaultPlugin();

	    String filePath = retryPort.execute(
		    eventId,
		    transformed.getPayload(),
		    () -> plugin.write(transformed)
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

	} catch (Exception e) {

	    auditPort.log(eventId, PipelineStatus.FAILED,
		    e.getMessage(), null);

	    auditPort.error(eventId, e);

	    return ResponseEntity.internalServerError()
		    .body(e.getMessage());
	}
    }
}