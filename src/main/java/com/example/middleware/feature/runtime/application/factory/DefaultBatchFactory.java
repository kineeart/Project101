package com.example.middleware.feature.runtime.application.factory;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.runtime.domain.BatchRecord;

@Service
public class DefaultBatchFactory
        implements BatchFactory {

    @Override
    public BatchRecord create(RawEvent event) {

        return new BatchRecord(
                event.getEventId()
        );
    }

}
