package com.example.middleware.feature.processing.application;
import com.example.middleware.feature.audit.application.port.AuditPort;
import com.example.middleware.feature.ingestion.application.ReceiveEventUseCase;
import com.example.middleware.feature.metadata.application.builder.MappingContextBuilder;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import com.example.middleware.feature.processing.domain.exception.DuplicateEventException;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.orchestration.domain.Execution;
import com.example.middleware.shared.enums.PipelineStatus;
import com.example.middleware.feature.orchestration.application.factory.ExecutionFactory;
import com.example.middleware.feature.ingestion.application.factory.EventFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.middleware.feature.orchestration.application.assembler.ResponseAssembler;
import java.util.Map;

@Service
public class ProcessingService implements ReceiveEventUseCase {
	private final ResponseAssembler responseAssembler;
	private final Pipeline pipeline;
    private final MappingContextBuilder mappingContextBuilder;
    private final AuditPort auditPort;
	private final ExecutionFactory executionFactory;
	private final PipelineContextBuilder pipelineContextBuilder;
	private final EventFactory eventFactory;
    public ProcessingService(
		ResponseAssembler responseAssembler,
	    MappingContextBuilder mappingContextBuilder,
	    AuditPort auditPort,
	  ExecutionFactory executionFactory,
	Pipeline pipeline,
	EventFactory eventFactory,
PipelineContextBuilder pipelineContextBuilder) {
	this.pipelineContextBuilder = pipelineContextBuilder;
this.executionFactory = executionFactory;
	this.mappingContextBuilder = mappingContextBuilder;
	this.auditPort = auditPort;
	this.responseAssembler = responseAssembler;
	this.pipeline = pipeline;
	this.eventFactory = eventFactory;
    }

	@Override
	public ResponseEntity<?> receive(Map<String, Object> request) {

	String eventId = "UNKNOWN";

	try {
	    

RawEvent event = eventFactory.create(request);

eventId = event.getEventId();
	    auditPort.log(eventId, PipelineStatus.RECEIVED,
		    "Webhook received", null);

	    

	    auditPort.log(eventId, PipelineStatus.VALIDATED,
		    "Validation passed", null);

		
Execution execution =
        executionFactory.create(eventId);




MappingContext mappingContext =
        mappingContextBuilder.build(
                event.getProfileId()
        );
PipelineContext pipelineContext =
        pipelineContextBuilder.build(
                event,
                mappingContext,
                execution
        );
StageResult result = pipeline.execute(pipelineContext);
if (result != StageResult.SUCCESS) {
    return ResponseEntity.internalServerError()
            .body("Pipeline execution failed.");
}




	   

	    return responseAssembler.success(pipelineContext);

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