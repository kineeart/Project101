package com.example.middleware.feature.intake.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.application.registry.ProcessingDispatcherRegistry;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.processing.domain.event.RawEvent;

@Service
public class DefaultReceiveEventUseCase implements ReceiveEventUseCase {

    private final EventRepositoryPort repository;
    private final ProcessingDispatcherRegistry registry; // Đổi tên thuộc tính từ dispatcher thành registry

    // Sửa tham số và gán giá trị chính xác cho registry trong Constructor
    public DefaultReceiveEventUseCase(
            EventRepositoryPort repository,
            ProcessingDispatcherRegistry registry) {
        this.repository = repository;
        this.registry = registry;
    }

    @Override
    public EventRecord receive(RawEvent event) {

        EventRecord record = new EventRecord(event);

        // Lưu bản ghi ban đầu (Trạng thái RECEIVED)
        repository.save(record);
        
        // Kích hoạt luồng xử lý thông qua registry.defaultDispatcher()
        registry
            .defaultDispatcher()
            .dispatch(record.getEventId());

        return record;
    }
}
