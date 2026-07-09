package com.example.middleware.feature.audit.application.port;

import java.util.List;

import com.example.middleware.feature.audit.domain.ErrorLog;
import com.example.middleware.feature.audit.domain.ProcessingLog;

public interface AuditRepositoryPort {

    void saveProcessingLog(ProcessingLog log);

    void saveErrorLog(ErrorLog log);

    List<ProcessingLog> findAllProcessing();

    List<ErrorLog> findAllErrors();
}