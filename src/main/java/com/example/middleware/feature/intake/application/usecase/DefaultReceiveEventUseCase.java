package com.example.middleware.feature.intake.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.application.port.ProcessingDispatcher;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.ingestion.application.ProcessEventUseCase; // Đã thêm import cho ProcessEventUseCase

@Service
public class DefaultReceiveEventUseCase implements ReceiveEventUseCase {

    private final EventRepositoryPort repository;
    private final ProcessingDispatcher dispatcher;

    // Inject cả repository và processEventUseCase qua Constructor
    public DefaultReceiveEventUseCase(
        EventRepositoryPort repository,
        ProcessingDispatcher dispatcher) {

    this.repository = repository;
    this.dispatcher = dispatcher;
}

    @Override
    public EventRecord receive(RawEvent event) {

        EventRecord record = new EventRecord(event);

        // Lưu bản ghi ban đầu (Trạng thái RECEIVED)
        repository.save(record);
        
        // Kích hoạt luồng xử lý đồng bộ (hoặc bất đồng bộ tùy cấu hình)
        dispatcher.dispatch(
        record.getEventId()
);

        return record;
    }
}
