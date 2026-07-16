package com.example.middleware.feature.delivery.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.middleware.feature.delivery.application.model.WorkspaceArtifact;
import com.example.middleware.feature.delivery.application.port.DeliveryArtifactRepository;
import com.example.middleware.feature.delivery.application.port.DeliveryPort;
import com.example.middleware.feature.delivery.application.port.OutputFileWriter;
import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.delivery.application.usecase.DefaultDeliveryUseCase;
import com.example.middleware.feature.delivery.domain.ArtifactStatus;
import com.example.middleware.feature.delivery.domain.DeliveryArtifact;
import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.processing.application.port.RetryPort;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

class DefaultDeliveryUseCaseTest {

    @Test
    void shouldDeliverSuccessfully() {
        // --- 1. ARRANGE ---
        
        // Khởi tạo Use Case với các Fake Objects nguyên bản
        FakeArtifactRepository repository = new FakeArtifactRepository();
        
        DefaultDeliveryUseCase useCase = new DefaultDeliveryUseCase(
                new FakeDeliveryPort(),
                new FakeOutputFileWriter(),
                new FakePublisher(),
                new FakeRetryPort(),
                repository
        );

        // Khởi tạo RawEvent đúng như runtime
        RawEvent rawEvent = new RawEvent();
        rawEvent.setEventId("EVT001");

        // Khởi tạo TransformedEvent với LinkedHashMap bảo toàn thứ tự dữ liệu
        TransformedEvent transformed = new TransformedEvent();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("ITEM", "A001");
        transformed.setPayload(payload);

        // Khởi tạo DeliveryProfile
        DeliveryProfile profile = new DeliveryProfile(
                "D:/ignore",
                "ORACLE_",
                "csv",
                ",",
                true,
                true,
                "TRAILER"
        );

        // Đóng gói vào MappingContext
        MappingContext mappingContext = new MappingContext();
        mappingContext.setDeliveryProfile(profile);

        // Lắp ráp toàn bộ vào PipelineContext trung tâm
        PipelineContext context = new PipelineContext();
        context.setRawEvent(rawEvent);
        context.setTransformedEvent(transformed);
        context.setMappingContext(mappingContext);

        // --- 2. ACT ---
        StageResult result = useCase.deliver(context);

        // --- 3. ASSERT (6 tiêu chí xác thực thiết kế) ---
        
        // ① StageResult phải báo SUCCESS
        assertEquals(StageResult.SUCCESS, result);

        // ② Context FilePath phải được cập nhật chính xác tên file
        assertEquals("ORACLE_TEST.csv", context.getFilePath());

        // ③ Repository ghi nhận lưu duy nhất 1 bản ghi lịch sử dữ liệu
        assertEquals(1, repository.findAll().size());

        // Rút bản ghi ra để thực hiện Business Assert chuyên sâu
        DeliveryArtifact artifact = repository.findAll().get(0);

        // ④ Trạng thái cuối cùng của file bắt buộc phải là PUBLISHED
        assertEquals(ArtifactStatus.PUBLISHED, artifact.getStatus());

        // ⑤ Đảm bảo tính toàn vẹn định danh sự kiện (EventId)
        assertEquals("EVT001", artifact.getEventId());

        // ⑥ Chứng minh assignFile() đã được gọi và liên kết thành công tên file từ bộ nhớ
        assertEquals("ORACLE_TEST.csv", artifact.getFileName());
    }

    // --- CÁC LỚP FAKE OBJECTS ĐƠN NHIỆM (KHÔNG MOCKITO / SPRING) ---

    private static class FakeDeliveryPort implements DeliveryPort {
        @Override
        public OutputFile build(TransformedEvent event, DeliveryProfile profile) {
            return new OutputFile(
                    "ORACLE_TEST.csv",
                    List.of("HEADER", "ROW", "TRAILER")
            );
        }
    }

    private static class FakeOutputFileWriter implements OutputFileWriter {
        @Override
        public WorkspaceArtifact write(OutputFile outputFile) {
            return new WorkspaceArtifact(
                    outputFile.fileName(),
                    "workspace/ORACLE_TEST.csv"
            );
        }
    }

    private static class FakePublisher implements OutputPublisher {
        @Override
        public void publish(WorkspaceArtifact workspaceArtifact, DeliveryProfile profile) {
            // Không làm gì, cô lập hoàn toàn các tác vụ IO mạng/đĩa
        }
    }

    private static class FakeRetryPort implements RetryPort {
        @Override
        public <T> T execute(String eventId, Map<String, Object> payload, RetryAction<T> action) {
            return action.execute();
        }
    }

    private static class FakeArtifactRepository implements DeliveryArtifactRepository {
        private final List<DeliveryArtifact> artifacts = new ArrayList<>();

        @Override
        public void save(DeliveryArtifact artifact) {
            artifacts.add(artifact);
        }

        @Override
        public List<DeliveryArtifact> findAll() {
            return artifacts;
        }
    }
}
