package com.example.middleware.feature.audit.application;

import com.example.middleware.feature.audit.application.port.AuditPort;
import com.example.middleware.feature.audit.application.port.AuditEventPort; // 1. THÊM IMPORT NÀY
import com.example.middleware.feature.audit.application.port.AuditRepositoryPort;
import com.example.middleware.feature.audit.domain.AuditEvent;
import com.example.middleware.feature.audit.domain.ErrorLog;
import com.example.middleware.feature.audit.domain.ProcessingLog;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent; // 2. THÊM IMPORT NÀY
import com.example.middleware.shared.enums.PipelineStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService implements AuditPort {

    private final AuditRepositoryPort repository;

    public AuditService(AuditRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void log(String eventId,
                    PipelineStatus status,
                    String message,
                    String filePath) {

        repository.saveProcessingLog(
                new ProcessingLog(eventId, status, message, filePath)
        );
    }

    @Override
    public void error(String eventId, Exception ex) {

        repository.saveErrorLog(
                new ErrorLog(
                        eventId,
                        ex.getClass().getSimpleName(),
                        ex.getMessage(),
                        ex.toString()
                )
        );
    }

    @Override
    public List<ProcessingLog> getProcessingLogs() {
        return repository.findAllProcessing();
    }

    @Override
    public List<ErrorLog> getErrorLogs() {
        return repository.findAllErrors();
    }

    /**
     * Execution Event -> Audit Event
     */
 public void recordExecutionEvent(ExecutionEvent event) {

    String activity =
            event.stageName() != null
                    ? event.stageName()
                    : "Pipeline";

    AuditEvent auditEvent =
            new AuditEvent(
                    event.executionId(),
                    activity,
                    event.type().name(),
                    event.message(),
                    event.occurredAt()
            );

    repository.saveAuditEvent(auditEvent);
}
@Override
public List<AuditEvent> getAuditEvents() {
    return repository.findAllAuditEvents();
}
}