package com.example.middleware.feature.processing.infrastructure.retry;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.processing.application.port.RetryDelay;

@Component
public class ThreadRetryDelay implements RetryDelay {

    @Override
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}