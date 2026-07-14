package com.example.middleware.feature.metadata.infrastructure.config;

import java.util.ArrayList;
import java.util.List;

public class MetadataProfileProperties {

    private String profileId;

    private String sourceSystem;

    private List<TableRuleProperties> tables =
            new ArrayList<>();

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public List<TableRuleProperties> getTables() {
        return tables;
    }

    public void setTables(
            List<TableRuleProperties> tables) {

        this.tables = tables;
    }
}