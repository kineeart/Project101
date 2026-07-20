package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.orchestration.application.PipelineContext; 
import com.example.middleware.feature.runtime.domain.BatchRecord; // Thêm import này
import org.springframework.stereotype.Service;

@Service
public class ProcessEventUseCase {

    private final EventRepositoryPort repository;
    private final PipelineContextBuilder contextBuilder;
    private final Pipeline pipeline;
    private final BatchRepositoryPort batchRepository;

    

    // Bước 1: Constructor đã inject đầy đủ thuộc tính đúng hướng dẫn
    public ProcessEventUseCase(
            EventRepositoryPort repository,
            BatchRepositoryPort batchRepository,
            PipelineContextBuilder contextBuilder,
            Pipeline pipeline) {
        this.repository = repository;
        this.batchRepository = batchRepository;
        this.contextBuilder = contextBuilder;
        this.pipeline = pipeline;
    }

    public void process(String eventId) {
        // Tìm Event và validate
        EventRecord record = repository.findById(eventId);
        if (record == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }

        // Bước 2: Lấy BatchRecord bằng eventId
        BatchRecord batch = batchRepository.findById(eventId);
        if (batch == null) {
            throw new IllegalArgumentException("Batch not found: " + eventId);
        }

        try {
            // Bước 3: Khi Worker nhận Batch -> Đổi sang PROCESSING
            // (Khôi phục quyền đổi trạng thái theo hướng dẫn mới)
            record.markProcessing();
            repository.save(record);

            batch.markProcessing();
            batchRepository.save(batch);

            // Build PipelineContext từ RawEvent của record
            PipelineContext context = contextBuilder.build(record.getRawEvent());

            // Bước 4: Trước Delivery -> Đổi sang WRITING
            batch.markWriting();
            batchRepository.save(batch);

            // Chạy Pipeline
            pipeline.execute(context);

            // Bước 5: Thành công -> Đổi sang WRITTEN
            record.markWritten(context.getFilePath());
            repository.save(record);

            batch.markWritten();
            batchRepository.save(batch);

        } catch (Exception ex) {
            // Bước 6: Thất bại -> Đổi sang FAILED
            record.markFailed(eventId);
            repository.save(record);

            batch.markFailed();
            batchRepository.save(batch);

            throw ex;
        }
    }
}
