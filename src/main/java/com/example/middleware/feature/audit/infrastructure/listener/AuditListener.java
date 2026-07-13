package com.example.middleware.feature.audit.infrastructure.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;

@Component
public class AuditListener {

    @EventListener
    public void onExecutionEvent(ExecutionEvent event) {

        System.out.println(
                "[AUDIT] "
                        + event.type()
                        + " | "
                        + event.executionId()
                        + " | "
                        + event.stageName()
        );
    }
}