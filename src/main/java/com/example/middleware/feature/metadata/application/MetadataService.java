package com.example.middleware.feature.metadata.application;

import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class MetadataService {

    private final MetadataRepository metadataRepository;

    public MetadataService(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

 public MappingContext loadMappingContext(String profileId) {

    EventMetadata metadata =
            metadataRepository.getEventMetadata(profileId);

    MappingContext context = new MappingContext();

    if (metadata == null) {
        return context;
    }

    for (TableRule rule : metadata.getTableRules()) {
        context.addRule(
                rule.getSourceTable(),
                rule
        );
    }

    return context;
}

    public EventMetadata resolveEventMetadata(String profileId) {
        return metadataRepository.getEventMetadata(profileId);
    }
}
