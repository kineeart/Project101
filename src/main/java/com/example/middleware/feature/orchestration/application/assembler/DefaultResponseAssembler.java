package com.example.middleware.feature.orchestration.application.assembler;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultResponseAssembler
        implements ResponseAssembler {

    @Override
public ResponseEntity<?> success(PipelineContext context) {

    if (context.getTransformedEvent() == null
            || context.getTransformedEvent().getPayload() == null
            || context.getTransformedEvent().getPayload().isEmpty()) {

        return ResponseEntity.badRequest()
                .body("No data after mapping.");
    }

    return ResponseEntity.ok(
            Map.of(
                    "status", "SUCCESS",
                    "eventId",
                    context.getRawEvent().getEventId(),
                    "filePath",
                    context.getFilePath()
            )
    );
}
}