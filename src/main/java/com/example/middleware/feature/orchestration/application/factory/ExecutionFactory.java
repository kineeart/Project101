package com.example.middleware.feature.orchestration.application.factory;

import com.example.middleware.feature.orchestration.domain.Execution;

public interface ExecutionFactory {

    Execution create(String eventId);

}
