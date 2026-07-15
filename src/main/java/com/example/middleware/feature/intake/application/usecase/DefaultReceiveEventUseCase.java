package com.example.middleware.feature.intake.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.processing.domain.event.RawEvent;


@Service
public class DefaultReceiveEventUseCase
        implements ReceiveEventUseCase {


    private final EventRepositoryPort repository;


    public DefaultReceiveEventUseCase(
            EventRepositoryPort repository) {

        this.repository = repository;
    }


    @Override
    public EventRecord receive(
            RawEvent event) {


        EventRecord record =
                new EventRecord(
                        event.getEventId(),
                        event.getProfileId()
                );


        repository.save(record);


        return record;
    }
}