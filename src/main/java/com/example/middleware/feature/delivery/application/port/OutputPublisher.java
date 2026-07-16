package com.example.middleware.feature.delivery.application.port;

import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;

public interface OutputPublisher {

    void publish(
    WorkspaceFile workspaceFile,
    DeliveryProfile profile
);

}