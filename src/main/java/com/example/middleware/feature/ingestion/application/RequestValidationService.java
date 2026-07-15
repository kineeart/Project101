package com.example.middleware.feature.ingestion.application;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class RequestValidationService {

    // Không dùng Constructor injection nữa vì class này giờ hoàn toàn không phụ thuộc runtime nào khác
    public RequestValidationService() {
    }

    public void validate(Map<String, Object> request) {
        // Chỉ kiểm tra tính toàn vẹn cơ bản của bao bì gói tin thô ở cổng vào
        if (request == null) {
            throw new IllegalArgumentException("Request body is null");
        }
        
        if (request.get("eventId") == null) {
            throw new IllegalArgumentException("Missing eventId");
        }
        
        // Hoàn toàn sạch bóng vòng lặp metadata lỗi thời
    }
}
