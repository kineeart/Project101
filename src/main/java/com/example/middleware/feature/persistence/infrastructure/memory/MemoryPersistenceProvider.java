package com.example.middleware.feature.persistence.infrastructure.memory;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.persistence.domain.PersistenceProvider;

@Component
public class MemoryPersistenceProvider implements PersistenceProvider {

    @Override
    public String getProviderName() {
        return "memory";
    }
}