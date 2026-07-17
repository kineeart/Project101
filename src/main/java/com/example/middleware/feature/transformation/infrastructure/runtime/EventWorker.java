package com.example.middleware.feature.transformation.infrastructure.runtime;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.application.port.ProcessingDispatcher;
import com.example.middleware.feature.intake.application.registry.ProcessingDispatcherRegistry;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.intake.domain.EventStatus;

@Component
public class EventWorker {

    private final EventRepositoryPort repository;
    private final ProcessingDispatcherRegistry dispatcherRegistry;

    public EventWorker(
            EventRepositoryPort repository,
            ProcessingDispatcherRegistry dispatcherRegistry) {

        this.repository = repository;
        this.dispatcherRegistry = dispatcherRegistry;
    }

    @Scheduled(fixedDelay = 1000)
    public void poll() {

        // Log khi worker bắt đầu mỗi chu kỳ quét (mỗi 1 giây)
        System.out.println("=== Worker Polling ===");

        List<EventRecord> events =
                repository.findByStatus(EventStatus.RECEIVED);

        ProcessingDispatcher dispatcher =
                dispatcherRegistry.defaultDispatcher();

        for (EventRecord event : events) {

            dispatcher.dispatch(event.getEventId());

        }

    }

}
