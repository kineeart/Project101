package com.example.middleware.feature.persistence.infrastructure.registry;

import com.example.middleware.feature.persistence.domain.PersistenceProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PersistenceRegistry {

    private final Map<String, PersistenceProvider> providers;

    public PersistenceRegistry(List<PersistenceProvider> providers) {

        this.providers = providers.stream()
                .collect(Collectors.toMap(
                        PersistenceProvider::getProviderName,
                        Function.identity()
                ));
    }

    public PersistenceProvider get(String providerName) {

        PersistenceProvider provider = providers.get(providerName);

        if (provider == null) {
            throw new IllegalArgumentException(
                    "Unknown persistence provider: " + providerName
            );
        }

        return provider;
    }
}