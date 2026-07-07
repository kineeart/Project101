package com.example.middleware.configuration;

import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.metadata.model.FieldRule;
import com.example.middleware.metadata.model.TableRule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MappingContextConfiguration {

    @Bean
    public MappingContext mappingContext() {

        MappingContext context =
                new MappingContext();

        TableRule rule =
                new TableRule();

        rule.setSourceTable(
                "HQ_Price_Master");

        rule.setTargetTable(
                "POS_Price_Target");

        List<FieldRule> fields =
                new ArrayList<>();

        FieldRule item =
                new FieldRule(
                        "Item_Code",
                        "ITEM_ID");

        item.setDataType("STRING");
        item.setRequired(true);

        FieldRule price =
                new FieldRule(
                        "Base_Price",
                        "PRICE");

        price.setDataType("DOUBLE");
        price.setRequired(true);

        FieldRule location =
                new FieldRule(
                        "LOCATION",
                        "LOCATION");

        location.setDataType("STRING");

        fields.add(item);
        fields.add(price);
        fields.add(location);

        rule.setFieldRules(fields);

        context.addRule(
                "HQ_Price_Master",
                rule);

        return context;
    }
}