package com.example.middleware.feature.metadata.application.port;

import com.example.middleware.feature.metadata.domain.EventMetadata;

public interface MetadataRepository {

    EventMetadata getEventMetadata(String profileId);

}