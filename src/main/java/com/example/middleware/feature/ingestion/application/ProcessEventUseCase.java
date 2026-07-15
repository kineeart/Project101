package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.orchestration.application.PipelineContext; // Hãy kiểm tra lại package chính xác của class này
import org.springframework.stereotype.Service;

@Service
public class ProcessEventUseCase {

    private final EventRepositoryPort repository;
    private final PipelineContextBuilder contextBuilder;
    private final Pipeline pipeline;

    // Bước 1: Constructor tương ứng để Spring tự động inject dependency
    public ProcessEventUseCase(
            EventRepositoryPort repository,
            PipelineContextBuilder contextBuilder,
            Pipeline pipeline) {
        this.repository = repository;
        this.contextBuilder = contextBuilder;
        this.pipeline = pipeline;
    }

    public void process(String eventId) {
        // Bước 2: Tìm Event và validate
        EventRecord record = repository.findById(eventId);

        if (record == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }

        // Bước 3: Đổi trạng thái sang PROCESSING và lưu lại
        record.markProcessing();
        repository.save(record);

        // Bước 8: Bao quanh bằng khối try/catch từ bước build context trở xuống
        try {
            // Bước 4 & 5: Lấy RawEvent từ record và Build PipelineContext
            PipelineContext context = contextBuilder.build(record.getRawEvent());

            // Bước 6: Chạy Pipeline
            pipeline.execute(context);

            // Bước 7: Hoàn thành, cập nhật filePath và lưu lại trạng thái COMPLETED
            record.complete(context.getFilePath());
            repository.save(record);

        } catch (Exception ex) {
            // Nếu xảy ra lỗi: chuyển trạng thái sang FAILED, lưu lại và throw tiếp ngoại lệ
            record.fail();
            repository.save(record);
            throw ex;
        }
    }
}
