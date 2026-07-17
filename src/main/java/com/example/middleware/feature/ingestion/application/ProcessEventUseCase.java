package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.orchestration.application.PipelineContext; 
import org.springframework.stereotype.Service;

@Service
public class ProcessEventUseCase {

    private final EventRepositoryPort repository;
    private final PipelineContextBuilder contextBuilder;
    private final Pipeline pipeline;

    public ProcessEventUseCase(
            EventRepositoryPort repository,
            PipelineContextBuilder contextBuilder,
            Pipeline pipeline) {
        this.repository = repository;
        this.contextBuilder = contextBuilder;
        this.pipeline = pipeline;
    }

    public void process(String eventId) {
        // Tìm Event và validate
        EventRecord record = repository.findById(eventId);

        if (record == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }

        // Quyền đổi trạng thái sang PROCESSING đã được xóa bỏ hoàn toàn tại đây.
        // Luồng xử lý chuyển thẳng sang bước build context và chạy pipeline.

        try {
            // Build PipelineContext từ RawEvent của record
            PipelineContext context = contextBuilder.build(record.getRawEvent());

            // Chạy Pipeline
            pipeline.execute(context);

            // Hoàn thành, cập nhật filePath và lưu lại trạng thái COMPLETED
            record.markWritten(
        context.getFilePath()
);

repository.save(record);
            repository.save(record);

        } catch (Exception ex) {
            // Nếu xảy ra lỗi: chuyển trạng thái sang FAILED, lưu lại và throw tiếp ngoại lệ
            record.markFailed(eventId);

repository.save(record);
            throw ex;
        }
    }
}
