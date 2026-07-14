package com.example.middleware.feature.ingestion.application.factory;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.metadata.application.MetadataService;
import com.example.middleware.feature.metadata.application.resolver.ProfileResolver;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.processing.domain.event.RawEvent;

@Component
public class DefaultEventFactory implements EventFactory {

    private final MetadataService metadataService;
    private final ProfileResolver profileResolver;

    // SỬA TẠI ĐÂY: Thêm thân hàm và gán giá trị cho các thuộc tính final
    public DefaultEventFactory(
        MetadataService metadataService,
        ProfileResolver profileResolver
    ) {
        this.metadataService = metadataService;
        this.profileResolver = profileResolver;
    }

    @Override
    public RawEvent create(Map<String, Object> request) {
        // Tránh lỗi NullPointerException nếu map không chứa key "eventId"
        String eventId = Objects.toString(request.get("eventId"), null);

        // Lấy dữ liệu metadata trực tiếp khi hàm create() được gọi
        String profileId = profileResolver.resolve(request);
System.out.println("Profile = " + profileId);

        EventMetadata metadata = metadataService.resolveEventMetadata(profileId);        
        System.out.println(metadata);
        return new RawEvent(
            eventId,
            metadata.getProfileId(),
           "HQ_Price_Master",
            request
        );
    }
}
