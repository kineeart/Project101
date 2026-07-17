package com.example.middleware.feature.monitoring.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.monitoring.application.port.ExecutionHistoryRepository;
import com.example.middleware.feature.monitoring.domain.ExecutionHistoryRecord;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;

@Component
public class ExecutionHistoryListener {

    private final ExecutionHistoryRepository repository;

    public ExecutionHistoryListener(
            ExecutionHistoryRepository repository) {

        this.repository = repository;
    }

    @EventListener
    public void handle(
            ExecutionEvent event) {

        ExecutionHistoryRecord record =
                new ExecutionHistoryRecord(
                        event.executionId(),
                        event.type(),
                        event.stageName(),
                        event.stageResult(),
                        event.message(),
                        event.occurredAt()
                );

        repository.save(record);
    }
}