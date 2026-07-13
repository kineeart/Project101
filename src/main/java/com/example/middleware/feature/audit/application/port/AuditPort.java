package com.example.middleware.feature.audit.application.port;

import java.util.List;

import com.example.middleware.feature.audit.domain.AuditEvent;
import com.example.middleware.feature.audit.domain.ErrorLog;
import com.example.middleware.feature.audit.domain.ProcessingLog;
import com.example.middleware.shared.enums.PipelineStatus;

public interface AuditPort {
    List<AuditEvent> getAuditEvents();
    void log(String eventId, PipelineStatus status, String message, String filePath);

    void error(String eventId, Exception ex);
    List<ProcessingLog> getProcessingLogs();

    List<ErrorLog> getErrorLogs();
}