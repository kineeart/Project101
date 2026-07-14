package com.example.middleware.feature.metadata.infrastructure.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "middleware.metadata")
public class MetadataProperties {

    private List<MetadataProfileProperties> profiles =
            new ArrayList<>();

    public List<MetadataProfileProperties> getProfiles() {
        return profiles;
    }

    public void setProfiles(
            List<MetadataProfileProperties> profiles) {

        this.profiles = profiles;
    }
}