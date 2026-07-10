package com.example.middleware.feature.orchestration.application.factory;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.orchestration.domain.Execution;

@Service
public class DefaultExecutionFactory
        implements ExecutionFactory {

    @Override
    public Execution create(String eventId) {
        return new Execution(eventId);
    }

}
