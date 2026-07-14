package com.example.middleware.feature.metadata.infrastructure.loader;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.metadata.application.port.MetadataLoader;
import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;

import jakarta.annotation.PostConstruct;

@Component
public class InMemoryMetadataLoader
        implements MetadataLoader {

    private final MetadataRepository repository;

    public InMemoryMetadataLoader(
            MetadataRepository repository) {

        this.repository = repository;
    }

    @PostConstruct
    @Override
    public void load() {
System.out.println("Metadata loaded.");
       TableRule tableRule = new TableRule();
tableRule.setSourceTable("HQ_Price_Master");
tableRule.setTargetTable("MNT");

FieldRule itemRule = new FieldRule();
itemRule.setSourceField("itemId");
itemRule.setTargetField("ITEM");

FieldRule priceRule = new FieldRule();
priceRule.setSourceField("price");
priceRule.setTargetField("PRICE");

tableRule.getFieldRules().add(itemRule);
tableRule.getFieldRules().add(priceRule);

EventMetadata profile =
        new EventMetadata(
                "PROFILE_1",
                "HQ",
                List.of(tableRule)
        );

repository.save(profile);

    }
    
}
