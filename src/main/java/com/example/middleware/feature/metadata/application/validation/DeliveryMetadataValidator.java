package com.example.middleware.feature.metadata.application.validation;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.metadata.infrastructure.config.DeliveryProperties;

@Component
public class DeliveryMetadataValidator
        implements MetadataValidator<DeliveryProperties> {


    @Override
    public void validate(
            DeliveryProperties delivery) {

        if (delivery == null) {
            throw new IllegalStateException(
                    "Delivery configuration is missing"
            );
        }


        if (isBlank(delivery.getOutputFolder())) {
            throw new IllegalStateException(
                    "Delivery configuration invalid: outputFolder is required"
            );
        }


        if (isBlank(delivery.getFilePrefix())) {
            throw new IllegalStateException(
                    "Delivery configuration invalid: filePrefix is required"
            );
        }


        if (isBlank(delivery.getExtension())) {
            throw new IllegalStateException(
                    "Delivery configuration invalid: extension is required"
            );
        }


        if (isBlank(delivery.getDelimiter())) {
            throw new IllegalStateException(
                    "Delivery configuration invalid: delimiter is required"
            );
        }

    }


    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

}