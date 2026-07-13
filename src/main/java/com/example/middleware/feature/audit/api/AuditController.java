package com.example.middleware.feature.audit.api;

import com.example.middleware.feature.audit.application.AuditService;
import com.example.middleware.feature.audit.application.port.AuditPort;
import com.example.middleware.feature.audit.domain.AuditEvent;
import com.example.middleware.feature.audit.domain.ErrorLog;
import com.example.middleware.feature.audit.domain.ProcessingLog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

    private final AuditPort auditPort;

    public AuditController(AuditPort auditPort) {
        this.auditPort = auditPort;
    }

    @GetMapping("/processing")
    public List<ProcessingLog> getProcessingLogs() {
        return auditPort.getProcessingLogs();
    }

    @GetMapping("/errors")
public List<ErrorLog> getErrorLogs() {
    return auditPort.getErrorLogs();
}
@GetMapping("/events")
public List<AuditEvent> getAuditEvents() {
    return auditPort.getAuditEvents();
}
}