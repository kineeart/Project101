package com.example.middleware.feature.processing.application.port;

import java.util.Map;

public interface RetryPort {

    String execute(String eventId, Map<String, Object> payload, RetryAction action);

    @FunctionalInterface
    interface RetryAction {
        String execute();
    }
}