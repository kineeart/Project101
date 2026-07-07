package com.example.middleware.feature.processing.application;

import com.example.middleware.feature.processing.application.port.MappingPort;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import com.example.middleware.feature.processing.domain.exception.MappingRuleNotFoundException;
import com.example.middleware.feature.processing.domain.transformation.DataTypeTransformer;
import com.example.middleware.feature.processing.domain.transformation.XrefTransformer;
import com.example.middleware.feature.processing.domain.validation.RequiredValidator;
import com.example.middleware.metadata.model.FieldRule;
import com.example.middleware.metadata.model.TableRule;
import org.springframework.stereotype.Service;

@Service
public class MappingEngine implements MappingPort {

    private final RequiredValidator validator =
            new RequiredValidator();

    private final DataTypeTransformer typeTransformer =
            new DataTypeTransformer();

    private final XrefTransformer xrefTransformer =
            new XrefTransformer();

    public TransformedEvent transform(
            RawEvent event,
            MappingContext context) {

        TableRule tableRule =
                context.getRule(event.getSourceTable());

        if (tableRule == null) {
            throw new MappingRuleNotFoundException(
                    "No mapping found for "
                            + event.getSourceTable()
            );
        }

        TransformedEvent result =
                new TransformedEvent();

        result.setProfileId(event.getProfileId());
        result.setTargetTable(
                tableRule.getTargetTable()
        );

        for (FieldRule rule :
                tableRule.getFieldRules()) {

            Object value =
                    event.getPayload()
                            .get(rule.getSourceField());

            if (value == null) {
                value = rule.getDefaultValue();
            }

            value =
                    xrefTransformer.transform(
                            value,
                            rule.getXrefDictionary()
                    );

            value =
                    typeTransformer.transform(
                            value,
                            rule.getDataType()
                    );

            validator.validate(rule, value);

            if (value != null) {
                result.getPayload().put(
                        rule.getTargetField(),
                        value
                );
            }
        }

        return result;
    }
}