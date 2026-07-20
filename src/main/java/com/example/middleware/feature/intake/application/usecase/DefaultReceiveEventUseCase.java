package com.example.middleware.feature.intake.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.runtime.application.factory.BatchFactory;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.domain.BatchRecord; // Nhớ import thêm class này

@Service
public class DefaultReceiveEventUseCase implements ReceiveEventUseCase {

    private final EventRepositoryPort repository;
    private final BatchFactory batchFactory;
    private final BatchRepositoryPort batchRepository;

    // Sửa Constructor: Inject cả 3 thuộc tính vào đây thay vì để = null
    public DefaultReceiveEventUseCase(
            EventRepositoryPort repository, 
            BatchFactory batchFactory, 
            BatchRepositoryPort batchRepository) {
        this.repository = repository;
        this.batchFactory = batchFactory;
        this.batchRepository = batchRepository;
    }

    @Override
    public EventRecord receive(RawEvent event) {

        // 1. Tạo và lưu EventRecord
        EventRecord record = new EventRecord(event);
        repository.save(record);
        
        // 2. Tạo BatchRecord từ rawEvent thông qua batchFactory
        BatchRecord batch = batchFactory.create(event);
        
        // 3. Lưu BatchRecord vào cơ sở dữ liệu qua batchRepository
        batchRepository.save(batch);
        
        // Trả về record để Controller phản hồi HTTP 202
        return record;
    }
}
