package com.example.middleware.feature.metadata.infrastructure.repository;

import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMetadataRepository implements MetadataRepository {

    @Override
    public EventMetadata getEventMetadata() {

        return new EventMetadata(
                "PROFILE_1",
                "HQ_Price_Master"
        );

    }

}