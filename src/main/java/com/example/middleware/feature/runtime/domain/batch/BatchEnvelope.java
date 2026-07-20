package com.example.middleware.feature.runtime.domain.batch;

import java.util.ArrayList;
import java.util.List;
import com.example.middleware.feature.processing.domain.event.RawEvent;

public class BatchEnvelope {

    private String batchId;
    private Integer version;
    private String sourceSystem;
    private List<RawEvent> events;

    // 1. Constructor mặc định (Hàm tạo rỗng) - Rất cần khi map dữ liệu từ JSON/Database
    public BatchEnvelope() {
        this.events = new ArrayList<>(); // Khởi tạo list rỗng tránh lỗi NullPointerException
    }

    // 2. Constructor đầy đủ tham số để khởi tạo nhanh đối tượng
    public BatchEnvelope(String batchId, Integer version, String sourceSystem, List<RawEvent> events) {
        this.batchId = batchId;
        this.version = version;
        this.sourceSystem = sourceSystem;
        this.events = events != null ? events : new ArrayList<>();
    }

    // ==========================================
    // CÁC HÀM NGHIỆP VỤ (DOMAIN METHODS) TIỆN ÍCH
    // ==========================================
    
    /**
     * Thêm một sự kiện đơn lẻ vào Batch
     */
    public void addEvent(RawEvent event) {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        if (event != null) {
            this.events.add(event);
        }
    }

    /**
     * Lấy ra tổng số lượng records/events có trong Batch này
     */
    public int getEventCount() {
        return this.events != null ? this.events.size() : 0;
    }

    /**
     * Kiểm tra xem Batch này có bị trống hay không
     */
    public boolean isEmpty() {
        return this.getEventCount() == 0;
    }

    // ==========================================
    // GETTERS & SETTERS (Đóng gói dữ liệu)
    // ==========================================

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public List<RawEvent> getEvents() {
        return events;
    }

    public void setEvents(List<RawEvent> events) {
        this.events = events != null ? events : new ArrayList<>();
    }
}
