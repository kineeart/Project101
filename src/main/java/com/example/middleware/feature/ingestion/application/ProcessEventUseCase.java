package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.orchestration.application.PipelineContext; 
import com.example.middleware.feature.runtime.domain.BatchRecord; 
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;
import com.example.middleware.feature.runtime.domain.BatchStatus; // SỬA Ở ĐÂY: Bỏ chữ ".batch" đi cho khớp với file History
import com.example.middleware.feature.runtime.application.port.BatchHistoryRepositoryPort; 

import org.springframework.stereotype.Service;

@Service
public class ProcessEventUseCase {

    private final EventRepositoryPort repository;
    private final PipelineContextBuilder contextBuilder;
    private final Pipeline pipeline;
    private final BatchRepositoryPort batchRepository;
    private final BatchHistoryRepositoryPort historyRepository; 

    public ProcessEventUseCase(
            EventRepositoryPort repository,
            BatchRepositoryPort batchRepository,
            BatchHistoryRepositoryPort historyRepository, 
            PipelineContextBuilder contextBuilder,
            Pipeline pipeline) {
        this.repository = repository;
        this.batchRepository = batchRepository;
        this.historyRepository = historyRepository; 
        this.contextBuilder = contextBuilder;
        this.pipeline = pipeline;
    }

    public void process(String eventId) {
        EventRecord record = repository.findById(eventId);
        if (record == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }

        BatchRecord batch = batchRepository.findById(eventId);
        if (batch == null) {
            throw new IllegalArgumentException("Batch not found: " + eventId);
        }

        try {
            record.markProcessing();
            repository.save(record);

            batch.markProcessing();
            batchRepository.save(batch); 

            // Hết lỗi: Cả 2 bên đã nhận diện chung một kiểu dữ liệu BatchStatus
            historyRepository.save(
                new BatchHistoryRecord(
                    batch.getBatchId(),
                    BatchStatus.PROCESSING,
                    "Worker claimed batch"
                )
            );

            PipelineContext context = contextBuilder.build(record.getRawEvent());

            batch.markWriting();
            batchRepository.save(batch); 

            historyRepository.save(
                new BatchHistoryRecord(
                    batch.getBatchId(),
                    BatchStatus.WRITING,
                    "Start writing output"
                )
            );

            pipeline.execute(context);

            record.markWritten(context.getFilePath());
            repository.save(record);

            batch.markWritten();
            batchRepository.save(batch); 

            historyRepository.save(
                new BatchHistoryRecord(
                    batch.getBatchId(),
                    BatchStatus.WRITTEN,
                    "Output written successfully"
                )
            );

        } catch (Exception ex) {
            record.markFailed(eventId);
            repository.save(record);

            batch.markFailed();
            batchRepository.save(batch); 

            historyRepository.save(
                new BatchHistoryRecord(
                    batch.getBatchId(),
                    BatchStatus.FAILED,
                    ex.getMessage()
                )
            );

            throw ex;
        }
    }
}
