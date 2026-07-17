package com.example.middleware.feature.ingestion.application.factory;

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

        // Lấy các trường định danh từ request gốc (Tránh dùng Objects.toString dễ sinh chuỗi "null")
        String eventId = request.get("eventId") != null ? request.get("eventId").toString() : null;
        String sourceTable = request.get("sourceTable") != null ? request.get("sourceTable").toString() : null;

        System.out.println("Extracted eventId: " + eventId);
        System.out.println("Extracted sourceTable: " + sourceTable);

        // Giải quyết thông tin profile và metadata
        String profileId = profileResolver.resolve(request);
        EventMetadata metadata = metadataService.resolveEventMetadata(profileId);        
        
        // Trích xuất cấu trúc payload lõi từ request gốc
        Map<String, Object> payload = (Map<String, Object>) request.get("payload");
        System.out.println("Extracted payload: " + payload);

        // [HƯỚNG SỬA NẾU VALIDATOR QUÉT TRONG PAYLOAD]: 
        // Nếu tầng sau (Validator) bị lỗi vì tìm eventId trong payload, ta chủ động bù lại eventId vào payload
        if (payload != null && !payload.containsKey("eventId") && eventId != null) {
            payload.put("eventId", eventId);
        }

        System.out.println("=== END DEBUG ===");

        return new RawEvent(
            eventId,
            metadata.getProfileId(),
            sourceTable,
            payload
        );
    }
}
