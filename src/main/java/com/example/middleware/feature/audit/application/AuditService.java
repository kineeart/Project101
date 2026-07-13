package com.example.middleware.feature.audit.application;

import com.example.middleware.feature.audit.application.port.AuditPort;
import com.example.middleware.feature.audit.application.port.AuditEventPort; // 1. THÊM IMPORT NÀY
import com.example.middleware.feature.audit.application.port.AuditRepositoryPort;
import com.example.middleware.feature.audit.domain.ErrorLog;
import com.example.middleware.feature.audit.domain.ProcessingLog;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent; // 2. THÊM IMPORT NÀY
import com.example.middleware.shared.enums.PipelineStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// 3. THÊM ", AuditEventPort" VÀO ĐÂY
public class AuditService implements AuditPort, AuditEventPort {

    private final AuditRepositoryPort repository;

    public AuditService(AuditRepositoryPort repository) {
        this.repository = repository;
    }

    @Override // Nên thêm @Override cho tường minh
    public void log(String eventId,
                    PipelineStatus status,
                    String message,
                    String filePath) {

        repository.saveProcessingLog(
                new ProcessingLog(eventId, status, message, filePath)
        );
    }

    @Override // Nên thêm @Override cho tường minh
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

    // 4. TRIỂN KHAI HÀM CỦA AuditEventPort VÀO ĐÂY
    @Override
    public void record(ExecutionEvent event) {
        // Viết code xử lý ghi nhận sự kiện ExecutionEvent tại đây
        // Ví dụ: repository.saveExecutionEvent(event); (Tùy thuộc vào việc AuditRepositoryPort của bạn có hàm này chưa)
    }
    public void recordExecutionEvent(ExecutionEvent event)
    {}
}
