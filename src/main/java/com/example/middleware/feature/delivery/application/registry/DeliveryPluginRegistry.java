package com.example.middleware.feature.delivery.application.registry;

import com.example.middleware.feature.delivery.application.port.DeliveryPlugin;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeliveryPluginRegistry {

    private final Map<String, DeliveryPlugin> plugins = new LinkedHashMap<>();

    public DeliveryPluginRegistry(List<DeliveryPlugin> plugins) {
        for (DeliveryPlugin plugin : plugins) {
            this.plugins.put(plugin.type(), plugin);
        }
    }

    public DeliveryPlugin defaultPlugin() {
        return plugins.values().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No delivery plugins available"));
    }

    public DeliveryPlugin get(String type) {
        DeliveryPlugin plugin = plugins.get(type);
        if (plugin == null) {
            throw new IllegalArgumentException("Unsupported delivery plugin: " + type);
        }
        return plugin;
    }
}