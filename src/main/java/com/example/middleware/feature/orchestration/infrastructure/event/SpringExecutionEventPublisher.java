package com.example.middleware.feature.orchestration.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.orchestration.application.event.ExecutionEventPublisher;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;

@Component
public class SpringExecutionEventPublisher
        implements ExecutionEventPublisher {
private final ApplicationEventPublisher applicationEventPublisher;
    public SpringExecutionEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {

    this.applicationEventPublisher = applicationEventPublisher;
}
@Override
public void publish(ExecutionEvent event) {

    applicationEventPublisher.publishEvent(event);

}
}