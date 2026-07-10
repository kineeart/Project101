package com.example.middleware.feature.metadata.domain;

public class EventMetadata {

    private final String profileId;
    private final String sourceSystem;

    public EventMetadata(String profileId, String sourceSystem) {
        this.profileId = profileId;
        this.sourceSystem = sourceSystem;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }
}