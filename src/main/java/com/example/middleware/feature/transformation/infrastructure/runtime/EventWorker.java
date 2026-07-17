package com.example.middleware.feature.transformation.infrastructure.runtime;

import java.util.List;
import java.util.Optional;

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

@Scheduled(fixedDelay = 3000)
public void poll() {

    ProcessingDispatcher dispatcher =
            dispatcherRegistry.defaultDispatcher();

    while (true) {

        Optional<EventRecord> claimed =
                repository.claimNext();

        if (claimed.isEmpty()) {
            break;
        }

        dispatcher.dispatch(
                claimed.get().getEventId()
        );
    }
}

}
