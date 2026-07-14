package com.example.middleware.feature.persistence.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.middleware.feature.persistence.application.PersistenceProviderFactory;

@RestController
@RequestMapping("/api/v1/persistence")
public class PersistenceController {

    private final PersistenceProviderFactory factory;

    public PersistenceController(
            PersistenceProviderFactory factory) {

        this.factory = factory;
    }

    @GetMapping
    public String provider() {
        return factory.getProvider().getProviderName();
    }
}