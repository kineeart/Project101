package com.example.middleware.feature.ingestion.api;

import com.example.middleware.feature.intake.application.usecase.GetEventStatusUseCase;
import com.example.middleware.feature.intake.application.usecase.ReceiveEventUseCase;
import com.example.middleware.feature.ingestion.application.factory.EventFactory;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.processing.domain.event.RawEvent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PriceEventController {

    private final GetEventStatusUseCase getEventStatusUseCase;
    private final EventFactory eventFactory;
    private final ReceiveEventUseCase receiveEventUseCase;

    public PriceEventController(
            EventFactory eventFactory,
            ReceiveEventUseCase receiveEventUseCase,
            GetEventStatusUseCase getEventStatusUseCase) {
        this.eventFactory = eventFactory;
        this.receiveEventUseCase = receiveEventUseCase;
        this.getEventStatusUseCase = getEventStatusUseCase;
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable String eventId) {
        EventRecord record = getEventStatusUseCase.get(eventId);

        // Sử dụng LinkedHashMap thay cho Map.of() để tránh NullPointerException khi filePath bị null
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("eventId", record.getEventId());
        response.put("status", record.getStatus());
        response.put("filePath", record.getFilePath());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/events")
    public ResponseEntity<?> receive(@RequestBody Map<String, Object> request) {
        RawEvent event = eventFactory.create(request);
        EventRecord record = receiveEventUseCase.receive(event);

        // Sử dụng LinkedHashMap để đảm bảo tính đồng nhất và an toàn dữ liệu
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("eventId", record.getEventId());
        response.put("status", record.getStatus());

        return ResponseEntity.accepted().body(response);
    }
}
