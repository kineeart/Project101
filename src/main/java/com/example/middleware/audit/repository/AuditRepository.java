package com.example.middleware.audit.repository;

import com.example.middleware.audit.model.ErrorLog;
import com.example.middleware.audit.model.ProcessingLog;
import com.example.middleware.feature.audit.application.port.AuditRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AuditRepository implements AuditRepositoryPort {

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
}