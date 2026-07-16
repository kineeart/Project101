package com.example.middleware.feature.delivery.domain;

import java.time.LocalDateTime;

public class DeliveryArtifact {

    private final String eventId;

    private String fileName;

    private ArtifactStatus status;

    private final LocalDateTime createdAt;

    public DeliveryArtifact(String eventId) {
        this.eventId = eventId;
        this.status = ArtifactStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Gán tên file cho Artifact sau khi Builder sinh tên thành công
     */
    public void assignFile(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Cập nhật trạng thái thành công khi Publisher chuyển file sang Inbound hoàn tất
     */
    public void markPublished() {
        this.status = ArtifactStatus.PUBLISHED;
    }

    /**
     * Cập nhật trạng thái thất bại nếu bất kỳ bước nào trong Pipeline gặp sự cố
     */
    public void markFailed() {
        this.status = ArtifactStatus.FAILED;
    }

    public String getEventId() {
        return eventId;
    }

    public String getFileName() {
        return fileName;
    }

    public ArtifactStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
