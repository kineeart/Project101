package com.example.middleware.feature.runtime.application.service;

public interface RuntimeStateService {
    void received(String batchId);
    void processing(String batchId);
    void validated(String batchId);
    void mapped(String batchId);
    void built(String batchId);
    void writing(String batchId);
    void written(String batchId);
    void failed(String batchId, String reason); // Thêm lý do lỗi nếu cần lưu lịch sử
}
