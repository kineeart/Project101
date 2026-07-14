package com.example.middleware.feature.metadata.application.builder;

import com.example.middleware.feature.metadata.application.MetadataService;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class DefaultMappingContextBuilder
        implements MappingContextBuilder {
private final MetadataService metadataService;
public DefaultMappingContextBuilder(
        MetadataService metadataService) {
    this.metadataService = metadataService;
}
    @Override
    public MappingContext build(String profileId) {
        return metadataService.loadMappingContext(profileId);

    }

}