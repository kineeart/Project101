package com.example.middleware.feature.metadata.infrastructure.loader;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.metadata.application.port.MetadataLoader;
import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;

import jakarta.annotation.PostConstruct;

@Component
public class InMemoryMetadataLoader
        implements MetadataLoader {

    private final MetadataRepository repository;

    public InMemoryMetadataLoader(
            MetadataRepository repository) {

        this.repository = repository;
    }

    @PostConstruct
    @Override
    public void load() {
System.out.println("Metadata loaded.");
       EventMetadata profile =
        new EventMetadata(
                "PROFILE_1",
                "HQ"
        );

repository.save(profile);
    }
    
}
