package com.example.middleware.feature.audit.infrastructure.repository;

import com.example.middleware.feature.audit.application.port.AuditRepositoryPort;
import com.example.middleware.feature.audit.domain.AuditEvent;
import com.example.middleware.feature.audit.domain.ErrorLog;
import com.example.middleware.feature.audit.domain.ProcessingLog;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryAuditRepository implements AuditRepositoryPort {
private final List<AuditEvent> auditEvents = new ArrayList<>();
    private final List<ProcessingLog> processingLogs = new ArrayList<>();
    private final List<ErrorLog> errorLogs = new ArrayList<>();

    public void saveProcessingLog(ProcessingLog log) {
        processingLogs.add(log);
    }

    public void saveErrorLog(ErrorLog log) {
        errorLogs.add(log);
    }

    public List<ProcessingLog> findAllProcessing() {
        return processingLogs;
    }

    public List<ErrorLog> findAllErrors() {
        return errorLogs;
    }

    public List<ProcessingLog> findByEventId(String eventId) {
        return processingLogs.stream()
                .filter(l -> l.getEventId().equals(eventId))
                .toList();
    }
    @Override
public void saveAuditEvent(AuditEvent event) {
    auditEvents.add(event);
}

@Override
public List<AuditEvent> findAllAuditEvents() {
    return List.copyOf(auditEvents);
}
}