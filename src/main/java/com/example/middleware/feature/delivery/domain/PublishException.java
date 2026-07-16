package com.example.middleware.feature.delivery.domain;

public class PublishException extends RuntimeException {

    public PublishException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}