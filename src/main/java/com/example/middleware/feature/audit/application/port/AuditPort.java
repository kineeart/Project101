package com.example.middleware.feature.audit.application.port;

import com.example.middleware.shared.enums.PipelineStatus;

public interface AuditPort {

    void log(String eventId, PipelineStatus status, String message, String filePath);

    void error(String eventId, Exception ex);
}