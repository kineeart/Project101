package com.example.middleware.feature.audit.application.port;

import com.example.middleware.audit.model.ErrorLog;
import com.example.middleware.audit.model.ProcessingLog;

import java.util.List;

public interface AuditRepositoryPort {

    void saveProcessingLog(ProcessingLog log);

    void saveErrorLog(ErrorLog log);

    List<ProcessingLog> findAllProcessing();

    List<ErrorLog> findAllErrors();
}