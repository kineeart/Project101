package com.example.middleware.feature.metadata.infrastructure.repository;

import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryMetadataRepository implements MetadataRepository {

    private final Map<String, EventMetadata> metadata = new HashMap<>();

    public InMemoryMetadataRepository() {
        // Chưa load dữ liệu ở đây nữa
    }

    public void add(EventMetadata eventMetadata) {

        metadata.put(
                eventMetadata.getProfileId(),
                eventMetadata
        );

    }

    @Override
    public EventMetadata getEventMetadata(String profileId) {
        return metadata.get(profileId);
    }
}