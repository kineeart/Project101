package com.example.middleware.feature.delivery.infrastructure.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.DeliveryArtifactRepository;
import com.example.middleware.feature.delivery.domain.DeliveryArtifact;

@Component
public class InMemoryDeliveryArtifactRepository
        implements DeliveryArtifactRepository {


    private final Map<String, DeliveryArtifact> artifacts =
            new ConcurrentHashMap<>();


    @Override
    public void save(
            DeliveryArtifact artifact) {

        artifacts.put(
                artifact.eventId(),
                artifact
        );
    }


    @Override
    public DeliveryArtifact findByEventId(
            String eventId) {

        return artifacts.get(eventId);
    }
}