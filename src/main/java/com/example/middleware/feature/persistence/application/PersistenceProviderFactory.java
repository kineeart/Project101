package com.example.middleware.feature.persistence.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.persistence.domain.PersistenceProvider;
import com.example.middleware.feature.persistence.infrastructure.config.PersistenceProperties;
import com.example.middleware.feature.persistence.infrastructure.registry.PersistenceRegistry;

@Service
public class PersistenceProviderFactory {

    private final PersistenceRegistry registry;

    private final PersistenceProperties properties;

    public PersistenceProviderFactory(
        PersistenceRegistry registry,
        PersistenceProperties properties) {

    this.registry = registry;
    this.properties = properties;
}

    public PersistenceProvider getProvider() {

    return registry.get(
            properties.provider()
    );
}

}
