package com.example.middleware.feature.delivery.application.service;

import com.example.middleware.feature.delivery.application.port.DeliveryPlugin;
import com.example.middleware.feature.delivery.application.port.DeliveryPort;
import com.example.middleware.feature.delivery.application.registry.DeliveryPluginRegistry;
import com.example.middleware.feature.delivery.domain.DeliveryResult;
import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import org.springframework.stereotype.Service;
@Service
public class DeliveryService implements DeliveryPort {

    private final DeliveryPluginRegistry registry;

    public DeliveryService(
            DeliveryPluginRegistry registry) {

        this.registry = registry;
    }

   @Override
public OutputFile build(
        TransformedEvent event,
        DeliveryProfile deliveryProfile) {

    DeliveryPlugin plugin =
            registry.defaultPlugin();

    return plugin.build(
            event,
            deliveryProfile
    );
}
}