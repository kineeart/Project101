package com.example.middleware.feature.metadata.infrastructure.loader;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.metadata.application.port.MetadataLoader;
import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.application.validation.DeliveryMetadataValidator;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;
import com.example.middleware.feature.metadata.infrastructure.config.MetadataProperties;
import com.example.middleware.feature.metadata.infrastructure.config.MetadataProfileProperties;
import com.example.middleware.feature.metadata.infrastructure.config.TableRuleProperties;
import com.example.middleware.feature.metadata.infrastructure.config.DeliveryProperties;
import com.example.middleware.feature.metadata.infrastructure.config.FieldRuleProperties;

import jakarta.annotation.PostConstruct;

@Component
public class InMemoryMetadataLoader
                implements MetadataLoader {
        private final DeliveryMetadataValidator deliveryValidator;
        private final MetadataRepository repository;
        private final MetadataProperties metadataProperties;

        public InMemoryMetadataLoader(
                        MetadataRepository repository,
                        MetadataProperties metadataProperties,
                        DeliveryMetadataValidator deliveryValidator) {

                this.repository = repository;
                this.metadataProperties = metadataProperties;
                this.deliveryValidator = deliveryValidator;
        }

        @PostConstruct
        @Override
        public void load() {

                System.out.println("Loading metadata...");

                for (MetadataProfileProperties profile : metadataProperties.getProfiles()) {
                        DeliveryProperties delivery = profile.getDelivery();

                        deliveryValidator.validate(delivery);
                        DeliveryProfile deliveryProfile = new DeliveryProfile(
                                        delivery.getOutputFolder(),
                                        delivery.getFilePrefix(),
                                        delivery.getExtension(),
                                        delivery.getDelimiter(),
                                        delivery.isIncludeHeader(),
                                        delivery.isIncludeTrailer(),
                                        delivery.getTrailerTemplate());
                        List<TableRule> tableRules = new ArrayList<>();

                        for (TableRuleProperties table : profile.getTables()) {

                                TableRule tableRule = new TableRule();

                                tableRule.setSourceTable(
                                                table.getSourceTable());

                                tableRule.setTargetTable(
                                                table.getTargetTable());

                                for (FieldRuleProperties field : table.getFields()) {
                                        System.out.println("-----");
                                        System.out.println(field.getSourceField());
                                        System.out.println(field.getTargetField());
                                        System.out.println(field.getDataType());
                                        System.out.println(field.isRequired());
                                        System.out.println(field.getDefaultValue());
                                        System.out.println(field. getXrefDictionary()); // hoặc getXrefDictionary()
                                        FieldRule rule = new FieldRule();
                                        rule.setXrefDictionary(field. getXrefDictionary());

                                        tableRule.getFieldRules().add(rule);
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

                                        rule.setXrefDictionary(
                                                        field. getXrefDictionary());
                                        System.out.println(rule.getXrefDictionary());
                                }

                                tableRules.add(tableRule);
                        }

                        EventMetadata metadata = new EventMetadata(
                                        profile.getProfileId(),
                                        profile.getSourceSystem(),
                                        tableRules, deliveryProfile);

                        repository.save(metadata);

                }

                System.out.println("Metadata loaded.");
        }

}
