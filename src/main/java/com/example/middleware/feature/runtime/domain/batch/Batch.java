package com.example.middleware.feature.runtime.domain.batch;

import java.time.LocalDateTime;

public class Batch {

    private final String batchId;
    private final String eventId;
    private BatchStatus status;
    private int totalRecord;
    private int validRecord;
    private int invalidRecord;
    private int writtenRecord;
    private int retryCount;
    private String leaseOwner;
    private LocalDateTime leaseUntil;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Constructor ---
    public Batch(String batchId, String eventId) {
        this.batchId = batchId;
        this.eventId = eventId;
        this.status = BatchStatus.RECEIVED;
        this.totalRecord = 0;
        this.validRecord = 0;
        this.invalidRecord = 0;
        this.writtenRecord = 0;
        this.retryCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // --- Business Methods (Các hàm thay đổi trạng thái & nghiệp vụ) ---
    
    public void markProcessing() {
        this.status = BatchStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateMetrics(int total, int valid, int invalid) {
        this.totalRecord = total;
        this.validRecord = valid;
        this.invalidRecord = invalid;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateWrittenCount(int writtenCount) {
        this.writtenRecord = writtenCount;
        this.updatedAt = LocalDateTime.now();
    }

    public void markWritten() {
        this.status = BatchStatus.WRITTEN;
        this.updatedAt = LocalDateTime.now();
    }

    public void markPartial() {
        this.status = BatchStatus.PARTIAL;
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = BatchStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }

    // Cơ chế phân phối (Lease/Claim) cho Worker chạy song song
    public void acquireLease(String owner, int durationInSeconds) {
        this.leaseOwner = owner;
        this.leaseUntil = LocalDateTime.now().plusSeconds(durationInSeconds);
        this.updatedAt = LocalDateTime.now();
    }

    public void releaseLease() {
        this.leaseOwner = null;
        this.leaseUntil = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementRetry() {
        this.retryCount++;
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---
    public String getBatchId() {
        return batchId;
    }

    public String getEventId() {
        return eventId;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public int getValidRecord() {
        return validRecord;
    }

    public int getInvalidRecord() {
        return invalidRecord;
    }

    public int getWrittenRecord() {
        return writtenRecord;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public String getLeaseOwner() {
        return leaseOwner;
    }

    public LocalDateTime getLeaseUntil() {
        return leaseUntil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

