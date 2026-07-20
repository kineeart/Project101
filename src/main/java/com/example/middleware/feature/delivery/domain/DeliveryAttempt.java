package com.example.middleware.feature.delivery.domain;

import java.time.LocalDateTime;

public class DeliveryAttempt {

    private final String batchId;
    private final int attemptNo;
    private DeliveryStatus status;
    private final LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String errorMessage;

    // Khởi tạo một lượt ghi mới (luôn bắt đầu bằng trạng thái WRITING)
    public DeliveryAttempt(String batchId, int attemptNo) {
        this.batchId = batchId;
        this.attemptNo = attemptNo;
        this.status = DeliveryStatus.WRITING;
        this.startedAt = LocalDateTime.now();
    }

    // Constructor đầy đủ (Phục vụ việc load dữ liệu từ Repository sau này nếu cần)
    public DeliveryAttempt(String batchId, int attemptNo, DeliveryStatus status, 
                           LocalDateTime startedAt, LocalDateTime finishedAt, String errorMessage) {
        this.batchId = batchId;
        this.attemptNo = attemptNo;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.errorMessage = errorMessage;
    }

    // --- Domain Methods ---
    public void markSuccess() {
        this.status = DeliveryStatus.SUCCESS;
        this.finishedAt = LocalDateTime.now();
    }

    public void markFailed(String error) {
        this.status = DeliveryStatus.FAILED;
        this.finishedAt = LocalDateTime.now();
        this.errorMessage = error;
    }

    // --- Getters ---
    public String getBatchId() { return batchId; }
    public int getAttemptNo() { return attemptNo; }
    public DeliveryStatus getStatus() { return status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public String getErrorMessage() { return errorMessage; }
}
