package com.example.middleware.feature.processing.application.port;

import java.util.Map;

public interface RetryPort {

    <T> T execute(
            String eventId,
            Map<String, Object> payload,
            RetryAction<T> action
    );

    @FunctionalInterface
    interface RetryAction<T> {

        T execute();

    }
}