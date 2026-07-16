package com.example.middleware.feature.delivery.application.port;

import java.nio.file.Path;

import com.example.middleware.feature.metadata.domain.DeliveryProfile;

public interface OutputPublisher {

void publish(
        Path workspaceFile,
        DeliveryProfile profile
);

}