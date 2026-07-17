package com.example.middleware.feature.ingestion.application.factory;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.metadata.application.MetadataService;
import com.example.middleware.feature.metadata.application.resolver.ProfileResolver;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.processing.domain.event.RawEvent;

@Component
public class DefaultEventFactory implements EventFactory {

    private final MetadataService metadataService;
    private final ProfileResolver profileResolver;

    public DefaultEventFactory(
        MetadataService metadataService,
        ProfileResolver profileResolver
    ) {
        this.metadataService = metadataService;
        this.profileResolver = profileResolver;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RawEvent create(Map<String, Object> request) {
        // [LOG DEBUG] In ra request gốc từ Postman để kiểm tra trực quan
        System.out.println("=== DEFAULT EVENT FACTORY DEBUG ===");
        System.out.println("Request gốc: " + request);

        // Lấy các trường định danh từ request gốc
        String eventId = request.get("eventId") != null ? request.get("eventId").toString() : null;
        String sourceTable = request.get("sourceTable") != null ? request.get("sourceTable").toString() : null;

        // 1. Trích xuất batchId (Mặc định dùng eventId nếu request không truyền lên)
        String batchId = request.get("batchId") != null ? request.get("batchId").toString() : eventId;

        // 2. Trích xuất version kiểu Integer (Mặc định là 1 nếu request không có hoặc lỗi định dạng)
        Integer version = 1;
        if (request.get("version") != null) {
            try {
                version = Integer.valueOf(request.get("version").toString());
            } catch (NumberFormatException e) {
                version = 1; 
            }
        }

        // 3. Trích xuất generatedAt kiểu LocalDateTime (Mặc định lấy thời gian hiện tại nếu trống)
        LocalDateTime generatedAt = LocalDateTime.now();
        if (request.get("generatedAt") != null) {
            try {
                generatedAt = LocalDateTime.parse(request.get("generatedAt").toString());
            } catch (Exception e) {
                generatedAt = LocalDateTime.now();
            }
        }

        System.out.println("Extracted eventId: " + eventId);
        System.out.println("Extracted sourceTable: " + sourceTable);
        System.out.println("Extracted batchId: " + batchId);
        System.out.println("Extracted version: " + version);
        System.out.println("Extracted generatedAt: " + generatedAt);

        // Giải quyết thông tin profile và metadata
        String profileId = profileResolver.resolve(request);
        EventMetadata metadata = metadataService.resolveEventMetadata(profileId);        
        
        // Trích xuất cấu trúc payload lõi từ request gốc
        Map<String, Object> payload = (Map<String, Object>) request.get("payload");
        System.out.println("Extracted payload: " + payload);

        // Nếu tầng sau (Validator) bị lỗi vì tìm eventId trong payload, ta chủ động bù lại eventId vào payload
        if (payload != null && !payload.containsKey("eventId") && eventId != null) {
            payload.put("eventId", eventId);
        }

        System.out.println("=== END DEBUG ===");

        // Gọi đúng Constructor của RawEvent: 
        // (String eventId, String profileId, String sourceTable, String BatchId, Integer version, LocalDateTime generatedAt, Map<String, Object> payload)
        return new RawEvent(
            eventId,
            metadata.getProfileId(),
            sourceTable,
            batchId,
            version,
            generatedAt,
            payload
        );
    }
}
