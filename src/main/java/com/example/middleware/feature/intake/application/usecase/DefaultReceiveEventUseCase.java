package com.example.middleware.feature.intake.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.processing.domain.event.RawEvent;

@Service
public class DefaultReceiveEventUseCase implements ReceiveEventUseCase {

    private final EventRepositoryPort repository;

    // Đã loại bỏ hoàn toàn ProcessingDispatcherRegistry vì không còn sử dụng
    public DefaultReceiveEventUseCase(EventRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public EventRecord receive(RawEvent event) {

        EventRecord record = new EventRecord(event);

        // Lưu bản ghi ban đầu (Trạng thái RECEIVED)
        repository.save(record);
        
        // Luồng xử lý bất đồng bộ (dispatcher) đã được gỡ bỏ tại đây
        // Request kết thúc ngay sau khi lưu thành công

        return record;
    }
}
