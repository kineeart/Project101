package com.example.middleware.feature.orchestration.infrastructure.event;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.orchestration.application.event.ExecutionEventPublisher;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;

@Component
public class SpringExecutionEventPublisher
        implements ExecutionEventPublisher {

    @Override
    public void publish(ExecutionEvent event) {
        // Intentionally empty.
        // Spring ApplicationEvent will be integrated in Sprint B3.
    }
}