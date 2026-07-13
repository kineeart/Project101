package com.example.middleware.feature.audit.infrastructure.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.audit.application.AuditService;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;

@Component
public class AuditListener {

    private final AuditService auditService;

    public AuditListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void onExecutionEvent(ExecutionEvent event) {
        auditService.recordExecutionEvent(event);
    }
}