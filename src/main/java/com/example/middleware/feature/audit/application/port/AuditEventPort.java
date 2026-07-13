package com.example.middleware.feature.audit.application.port;

import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;

public interface AuditEventPort {

    void record(ExecutionEvent event);

}
