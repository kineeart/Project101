package com.example.middleware.feature.delivery.application.port;


import com.example.middleware.feature.delivery.application.model.WorkspaceArtifact;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;

public interface OutputPublisher {

    void publish(

        WorkspaceArtifact artifact,

        DeliveryProfile profile

);

}