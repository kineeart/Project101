package com.example.middleware.feature.delivery.infrastructure.publisher;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;

@Component
public class AtomicOutputPublisher implements OutputPublisher
 {

 @Override
public void publish(
        Path workspaceFile,
        DeliveryProfile profile
)
{}
}
