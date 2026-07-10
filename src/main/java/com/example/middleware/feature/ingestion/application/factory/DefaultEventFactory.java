package com.example.middleware.feature.ingestion.application.factory;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.metadata.application.MetadataService;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.processing.domain.event.RawEvent;

@Component
public class DefaultEventFactory implements EventFactory {

    private final MetadataService metadataService;

    public DefaultEventFactory(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @Override
    public RawEvent create(Map<String, Object> request) {
        // Tránh lỗi NullPointerException nếu map không chứa key "eventId"
        String eventId = Objects.toString(request.get("eventId"), null);

        // Lấy dữ liệu metadata trực tiếp khi hàm create() được gọi
        EventMetadata metadata =
        metadataService.resolveEventMetadata();

        return new RawEvent(
        eventId,
        metadata.getProfileId(),
        metadata.getSourceSystem(),
        request
);
    }
}
