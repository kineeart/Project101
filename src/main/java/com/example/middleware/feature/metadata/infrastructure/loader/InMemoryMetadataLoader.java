package com.example.middleware.feature.metadata.infrastructure.loader;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.metadata.application.port.MetadataLoader;
import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;
import com.example.middleware.feature.metadata.infrastructure.config.MetadataProperties;
import com.example.middleware.feature.metadata.infrastructure.config.MetadataProfileProperties;
import com.example.middleware.feature.metadata.infrastructure.config.TableRuleProperties;
import com.example.middleware.feature.metadata.infrastructure.config.FieldRuleProperties;

import jakarta.annotation.PostConstruct;

@Component
public class InMemoryMetadataLoader
        implements MetadataLoader {

    private final MetadataRepository repository;
private final MetadataProperties metadataProperties;

public InMemoryMetadataLoader(
        MetadataRepository repository,
        MetadataProperties metadataProperties) {

    this.repository = repository;
    this.metadataProperties = metadataProperties;
}

    @PostConstruct
@Override
public void load() {

    System.out.println("Loading metadata...");

    for (MetadataProfileProperties profile :
            metadataProperties.getProfiles()) {

        List<TableRule> tableRules = new ArrayList<>();

        for (TableRuleProperties table :
                profile.getTables()) {

            TableRule tableRule = new TableRule();

            tableRule.setSourceTable(
                    table.getSourceTable());

            tableRule.setTargetTable(
                    table.getTargetTable());

            for (FieldRuleProperties field :
                    table.getFields()) {

                FieldRule rule = new FieldRule();

                rule.setSourceField(
                        field.getSourceField());

                rule.setTargetField(
                        field.getTargetField());

                rule.setDataType(
                        field.getDataType());

                rule.setRequired(
                        field.isRequired());

                rule.setDefaultValue(
                        field.getDefaultValue());

                tableRule.getFieldRules().add(rule);
            }

            tableRules.add(tableRule);
        }

        EventMetadata metadata =
                new EventMetadata(
                        profile.getProfileId(),
                        profile.getSourceSystem(),
                        tableRules
                );

        repository.save(metadata);
    }

    System.out.println("Metadata loaded.");
}
    
}
