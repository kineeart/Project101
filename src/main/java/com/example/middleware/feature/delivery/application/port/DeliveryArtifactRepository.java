package com.example.middleware.feature.delivery.application.port;

import com.example.middleware.feature.delivery.domain.DeliveryArtifact;

public interface DeliveryArtifactRepository {

    void save(
            DeliveryArtifact artifact
    );

    DeliveryArtifact findByEventId(
            String eventId
    );
}