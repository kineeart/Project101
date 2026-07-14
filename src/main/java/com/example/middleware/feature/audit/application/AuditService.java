package com.example.middleware.feature.audit.application;

import com.example.middleware.feature.audit.application.port.AuditPort;
import com.example.middleware.feature.audit.application.port.AuditEventPort;
import com.example.middleware.feature.audit.application.port.AuditRepositoryPort;
import com.example.middleware.feature.audit.domain.AuditEvent;
import com.example.middleware.feature.audit.domain.ErrorLog;
import com.example.middleware.feature.audit.domain.ProcessingLog;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;
import com.example.middleware.shared.enums.PipelineStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService implements AuditPort, AuditEventPort {

    // 1. Đặt Constant ở đầu class theo đúng chuẩn Clean Code
    private static final String PIPELINE_ACTIVITY = "Pipeline";

    private final AuditRepositoryPort repository;

    public AuditService(AuditRepositoryPort repository) {
        this.repository = repository;
    }

    // ==========================================
    // CÁC HÀM THUỘC AUDITPORT (Legacy / P1)
    // ==========================================

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

    // ==========================================
    // CÁC HÀM THUỘC AUDITEVENTPORT (Runtime Audit)
    // ==========================================

    @Override
    public void record(ExecutionEvent event) {
        // Xác định activity: lấy stageName nếu có, ngược lại dùng hằng số "Pipeline"
        String activity = event.stageName() != null 
                ? event.stageName() 
                : PIPELINE_ACTIVITY;

        // Khởi tạo thực thể AuditEvent từ dữ liệu của ExecutionEvent
        AuditEvent auditEvent = new AuditEvent(
                event.executionId(),
                activity,
                resolveOutcome(event), // Dùng hàm bổ trợ để chuẩn hóa kết quả đầu ra
                event.message(),
                event.occurredAt()
        );

        repository.saveAuditEvent(auditEvent);
    }

    @Override
    public List<AuditEvent> getAuditEvents() {
        return repository.findAllAuditEvents();
    }

    // ==========================================
    // HÀM BỔ TRỢ (HELPER METHODS)
    // ==========================================

    private String resolveOutcome(ExecutionEvent event) {
        switch (event.type()) {
            case STARTED:
                return "STARTED";

            case COMPLETED:
                return "SUCCESS";

            case FAILED:
                return "FAILED";

            case STAGE_FINISHED:
                if (event.stageResult() != null) {
                    return event.stageResult().name();
                }
                return "SUCCESS";

            default:
                return event.type().name();
        }
    }
}
