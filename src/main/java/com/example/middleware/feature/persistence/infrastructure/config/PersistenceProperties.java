package com.example.middleware.feature.persistence.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PersistenceProperties {

    @Value("${persistence.provider}")
    private String provider;

    public String provider() {
        return provider;
    }
}