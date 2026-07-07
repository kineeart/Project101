package com.example.middleware.feature.ingestion.application;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RequestValidationService {

    public void validate(
            Map<String, Object> request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Request body is null");
        }

        if (request.get("eventId") == null) {
            throw new IllegalArgumentException(
                    "Missing eventId");
        }

        if (request.get("Item_Code") == null) {
            throw new IllegalArgumentException(
                    "Missing Item_Code");
        }

        Object price =
                request.get("Base_Price");

        if (price == null) {
            throw new IllegalArgumentException(
                    "Missing Base_Price");
        }

        if (!(price instanceof Number)) {
            throw new IllegalArgumentException(
                    "Base_Price must be numeric");
        }

        double value =
                ((Number) price).doubleValue();

        if (value <= 0) {
            throw new IllegalArgumentException(
                    "Base_Price must be greater than 0");
        }
    }
}