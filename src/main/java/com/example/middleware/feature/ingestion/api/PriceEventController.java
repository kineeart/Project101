package com.example.middleware.feature.ingestion.api;
import com.example.middleware.feature.intake.application.usecase.ReceiveEventUseCase;
import com.example.middleware.feature.ingestion.application.factory.EventFactory;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.EventOrchestrator;
import com.example.middleware.feature.processing.domain.event.RawEvent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PriceEventController {

    private final EventFactory eventFactory;

private final ReceiveEventUseCase receiveEventUseCase;


public PriceEventController(
        EventFactory eventFactory,
        ReceiveEventUseCase receiveEventUseCase) {

    this.eventFactory = eventFactory;
    this.receiveEventUseCase = receiveEventUseCase;
}

@PostMapping("/events")
public ResponseEntity<?> receive(
        @RequestBody Map<String,Object> request) {


    RawEvent event =
            eventFactory.create(request);


    EventRecord record =
            receiveEventUseCase.receive(event);


    return ResponseEntity
            .accepted()
            .body(
                    Map.of(
                            "eventId",
                            record.getEventId(),

                            "status",
                            record.getStatus()
                    )
            );
}
}