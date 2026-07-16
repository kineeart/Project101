package com.example.middleware.feature.delivery.infrastructure.repository;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.DeliveryArtifactRepository;
import com.example.middleware.feature.delivery.domain.DeliveryArtifact;


@Component
public class InMemoryDeliveryArtifactRepository 
        implements DeliveryArtifactRepository {


    private final List<DeliveryArtifact> artifacts =
            new ArrayList<>();


    @Override
    public void save(
            DeliveryArtifact artifact) {

        artifacts.add(artifact);
    }


    @Override
    public List<DeliveryArtifact> findAll() {

        return List.copyOf(artifacts);
    }
}