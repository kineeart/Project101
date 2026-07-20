package com.example.middleware.feature.runtime.domain;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus; 
import java.time.LocalDateTime;

public class BatchRecord {

    private final String batchId;
    private BatchStatus status;
    private int totalRecords;
    private int validRecords;
    private int invalidRecords;
    private int writtenRecords;
    private int retryCount;
    private LocalDateTime receivedAt;
    private LocalDateTime updatedAt;

    // Constructor bắt buộc phải có để khởi tạo trường 'final batchId'
    public BatchRecord(String batchId) {
        this.batchId = batchId;
        this.status = BatchStatus.RECEIVED; 
        this.receivedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor đầy đủ tham số
    public BatchRecord(String batchId, BatchStatus status, int totalRecords, int validRecords, 
                       int invalidRecords, int writtenRecords, int retryCount, 
                       LocalDateTime receivedAt, LocalDateTime updatedAt) {
        this.batchId = batchId;
        this.status = status;
        this.totalRecords = totalRecords;
        this.validRecords = validRecords;
        this.invalidRecords = invalidRecords;
        this.writtenRecords = writtenRecords;
        this.retryCount = retryCount;
        this.receivedAt = receivedAt;
        this.updatedAt = updatedAt;
    }

   // Trong file BatchRecord.java: Tìm hàm changeStatus và sửa chính xác thành public như thế này:
public void changeStatus(com.example.middleware.feature.runtime.domain.batch.BatchStatus status) {
    this.status = status;
    this.updatedAt = java.time.LocalDateTime.now();
}

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================

    public String getBatchId() {
        return batchId;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now(); 
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getValidRecords() {
        return validRecords;
    }

    public void setValidRecords(int validRecords) {
        this.validRecords = validRecords;
    }

    public int getInvalidRecords() {
        return invalidRecords;
    }

    public void setInvalidRecords(int invalidRecords) {
        this.invalidRecords = invalidRecords;
    }

    public int getWrittenRecords() {
        return writtenRecords;
    }

    public void setWrittenRecords(int writtenRecords) {
        this.writtenRecords = writtenRecords;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
