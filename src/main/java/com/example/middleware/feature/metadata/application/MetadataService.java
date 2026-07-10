package com.example.middleware.feature.metadata.application;

import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import org.springframework.stereotype.Service;
@Service
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
public EventMetadata resolveEventMetadata() {

   return metadataRepository.getEventMetadata();
}
}