package com.example.middleware.feature.audit.infrastructure.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.audit.application.AuditService;
import com.example.middleware.feature.audit.application.port.AuditEventPort;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;

@Component
public class AuditListener {

    private final AuditEventPort auditEventPort;

public AuditListener(AuditEventPort auditEventPort) {
    this.auditEventPort = auditEventPort;
}

  @EventListener
public void onExecutionEvent(ExecutionEvent event) {
    auditEventPort.record(event);
}
}