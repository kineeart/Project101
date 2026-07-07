package com.example.middleware.delivery.factory;

import com.example.middleware.delivery.strategy.OutputWriterStrategy;
import com.example.middleware.feature.delivery.application.registry.DeliveryPluginRegistry;
import com.example.middleware.feature.delivery.infrastructure.plugin.DataLoaderMntPlugin;
import org.springframework.stereotype.Component;

@Component
public class OutputWriterFactory {

    private final DeliveryPluginRegistry registry;

    public OutputWriterFactory() {
        this.registry = null;
    }

    public OutputWriterFactory(DeliveryPluginRegistry registry) {
        this.registry = registry;
    }

    public OutputWriterStrategy getWriter(String type) {
        if (registry != null) {
            return (OutputWriterStrategy) registry.get(type);
        }

        if ("ORACLE_CSV".equals(type)) {
            return new DataLoaderMntPlugin();
        }

        throw new IllegalArgumentException("Unsupported output type: " + type);
    }
}