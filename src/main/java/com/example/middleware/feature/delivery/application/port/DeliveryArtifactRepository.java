package com.example.middleware.feature.delivery.application.port;

import com.example.middleware.feature.delivery.domain.DeliveryArtifact;

import java.util.List;

public interface DeliveryArtifactRepository {


    void save(
            DeliveryArtifact artifact
    );


    List<DeliveryArtifact> findAll();


}