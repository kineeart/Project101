package com.example.middleware.feature.processing.domain.transformation;

public class DataTypeTransformer {

    public Object transform(
            Object value,
            String dataType) {

        if (value == null) {
            return null;
        }

        if (dataType == null) {
            return value;
        }

        try {
            switch (dataType.toUpperCase()) {

                case "STRING":
                    return value.toString();

                case "INTEGER":
                    return Integer.parseInt(
                            value.toString());

                case "DOUBLE":
                    return Double.parseDouble(
                            value.toString());

                case "BOOLEAN":
                    return Boolean.parseBoolean(
                            value.toString());

                default:
                    return value;
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException(
                    "Cannot convert value ["
                            + value
                            + "] to type ["
                            + dataType
                            + "]"
            );
        }
    }
}