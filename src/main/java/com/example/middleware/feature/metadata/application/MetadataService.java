package com.example.middleware.feature.metadata.application;

import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.processing.domain.context.MappingContext;

public class MetadataService {
    private final MetadataRepository metadataRepository;
    public MetadataService(
        MetadataRepository metadataRepository) {
    this.metadataRepository = metadataRepository;
}
public MappingContext loadMappingContext(String profileId) {

    // Phase hiện tại:
    // Repository chưa trả dữ liệu thật.

    metadataRepository.toString();

    return new MappingContext();
}
}