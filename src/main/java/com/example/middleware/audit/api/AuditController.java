package com.example.middleware.audit.api;

import com.example.middleware.audit.model.ErrorLog;
import com.example.middleware.audit.model.ProcessingLog;
import com.example.middleware.audit.service.AuditService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/processing")
    public List<ProcessingLog> getProcessingLogs() {
        return auditService.getProcessingLogs();
    }

    @GetMapping("/errors")
    public List<ErrorLog> getErrorLogs() {
        return auditService.getErrorLogs();
    }
}