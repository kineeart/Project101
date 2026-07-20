package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.orchestration.application.PipelineContext; 
import com.example.middleware.feature.runtime.domain.BatchRecord; 
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus; 
import com.example.middleware.feature.runtime.application.port.BatchHistoryRepositoryPort; 
import com.example.middleware.feature.runtime.domain.state.RuntimeStateMachine; // THÊM IMPORT: Để hiểu State Machine

import org.springframework.stereotype.Service;

@Service
public class ProcessEventUseCase {

    private final EventRepositoryPort repository;
    private final PipelineContextBuilder contextBuilder;
    private final Pipeline pipeline;
    private final BatchRepositoryPort batchRepository;
    private final BatchHistoryRepositoryPort historyRepository; 
    private final RuntimeStateMachine stateMachine; // 1. INJECT THÊM BIẾN STATE MACHINE

    // 2. CẬP NHẬT CONSTRUCTOR: Nhận thêm RuntimeStateMachine từ Spring
    public ProcessEventUseCase(
            EventRepositoryPort repository,
            BatchRepositoryPort batchRepository,
            BatchHistoryRepositoryPort historyRepository, 
            PipelineContextBuilder contextBuilder,
            Pipeline pipeline,
            RuntimeStateMachine stateMachine) { // Nhận thêm ở đây
        this.repository = repository;
        this.batchRepository = batchRepository;
        this.historyRepository = historyRepository; 
        this.contextBuilder = contextBuilder;
        this.pipeline = pipeline;
        this.stateMachine = stateMachine; // Gán giá trị vào biến final
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
            // Bước 3: Khi Worker nhận Batch -> Đổi sang PROCESSING qua State Machine
            record.markProcessing();
            repository.save(record);

            // THAY ĐỔI: Sử dụng stateMachine để kiểm duyệt và đổi trạng thái thay vì gọi hàm mark trực tiếp
            stateMachine.transit(batch, BatchStatus.PROCESSING);
            batchRepository.save(batch); 

            historyRepository.save(
                new BatchHistoryRecord(
                    batch.getBatchId(),
                    BatchStatus.PROCESSING,
                    "Worker claimed batch"
                )
            );

            // Build PipelineContext từ RawEvent của record
            PipelineContext context = contextBuilder.build(record.getRawEvent());

            // Bước 4: Trước Delivery -> Đổi sang WRITING qua State Machine
            stateMachine.transit(batch, BatchStatus.WRITING); // THAY ĐỔI Ở ĐÂY
            batchRepository.save(batch); 

            historyRepository.save(
                new BatchHistoryRecord(
                    batch.getBatchId(),
                    BatchStatus.WRITING,
                    "Start writing output"
                )
            );

            // Chạy Pipeline
            pipeline.execute(context);

            // Bước 5: Thành công -> Đổi sang WRITTEN qua State Machine
            record.markWritten(context.getFilePath());
            repository.save(record);

            stateMachine.transit(batch, BatchStatus.WRITTEN); // THAY ĐỔI Ở ĐÂY
            batchRepository.save(batch); 

            historyRepository.save(
                new BatchHistoryRecord(
                    batch.getBatchId(),
                    BatchStatus.WRITTEN,
                    "Output written successfully"
                )
            );

        } catch (Exception ex) {
            // Bước 6: Thất bại -> Đổi sang FAILED qua State Machine
            record.markFailed(eventId);
            repository.save(record);

            stateMachine.transit(batch, BatchStatus.FAILED); // THAY ĐỔI Ở ĐÂY
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
