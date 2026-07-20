package com.example.middleware.feature.runtime.application.factory;

import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.runtime.domain.BatchRecord;

public interface BatchFactory {

    BatchRecord create(RawEvent event);

}