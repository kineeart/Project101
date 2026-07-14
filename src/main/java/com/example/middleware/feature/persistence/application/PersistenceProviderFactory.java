package com.example.middleware.feature.persistence.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.persistence.domain.PersistenceProvider;
import com.example.middleware.feature.persistence.infrastructure.config.PersistenceProperties;

@Service
public class PersistenceProviderFactory {

    private final List<PersistenceProvider> providers;

    private final PersistenceProperties properties;

    public PersistenceProviderFactory(
            List<PersistenceProvider> providers,
            PersistenceProperties properties) {

        this.providers = providers;
        this.properties = properties;
    }

    public PersistenceProvider getProvider() {

        String selected = properties.provider();

        return providers.stream()
                .filter(p -> p.getProviderName().equals(selected))
                .findFirst()
                .orElseThrow(() ->
        new IllegalArgumentException(
                "Unknown persistence provider: " + selected
        ));
    }

}
