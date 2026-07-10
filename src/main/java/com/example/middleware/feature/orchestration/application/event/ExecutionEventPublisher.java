package com.example.middleware.feature.orchestration.application.event;

import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;

public interface ExecutionEventPublisher {

    void publish(ExecutionEvent event);

}