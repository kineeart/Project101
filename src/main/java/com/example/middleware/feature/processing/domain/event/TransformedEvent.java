package com.example.middleware.feature.processing.domain.event;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TransformedEvent {

    private String profileId;
    private String targetTable;
    private Map<String, Object> payload = new LinkedHashMap<>();
    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}