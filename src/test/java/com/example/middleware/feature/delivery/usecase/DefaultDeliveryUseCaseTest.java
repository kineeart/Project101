package com.example.middleware.feature.delivery.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        // --- ARRANGE ---
        FakeArtifactRepository repository = new FakeArtifactRepository();
        DefaultDeliveryUseCase useCase = new DefaultDeliveryUseCase(
                new FakeDeliveryPort(), new FakeOutputFileWriter(), new FakePublisher(), new FakeRetryPort(), repository
        );

        RawEvent rawEvent = new RawEvent();
        rawEvent.setEventId("EVT001");

        TransformedEvent transformed = new TransformedEvent();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("ITEM", "A001");
        transformed.setPayload(payload);

        DeliveryProfile profile = new DeliveryProfile("D:/ignore", "ORACLE_", "csv", ",", true, true, "TRAILER");
        MappingContext mappingContext = new MappingContext();
        mappingContext.setDeliveryProfile(profile);

        PipelineContext context = new PipelineContext();
        context.setRawEvent(rawEvent);
        context.setTransformedEvent(transformed);
        context.setMappingContext(mappingContext);

        // --- ACT ---
        StageResult result = useCase.deliver(context);

        // --- ASSERT ---
        assertEquals(StageResult.SUCCESS, result);
        assertEquals("ORACLE_TEST.csv", context.getFilePath());
        assertEquals(1, repository.findAll().size());
        DeliveryArtifact artifact = repository.findAll().get(0);
        assertEquals(ArtifactStatus.PUBLISHED, artifact.getStatus());
        assertEquals("EVT001", artifact.getEventId());
        assertEquals("ORACLE_TEST.csv", artifact.getFileName());
    }

    // TÂM ĐIỂM: TEST CASE THỨ 2 THEO HƯỚNG DẪN TECH LEAD
    @Test
    void shouldMarkArtifactFailedWhenPublishFails() {
        // --- 1. ARRANGE ---
        FakeArtifactRepository repository = new FakeArtifactRepository();
        
        // Thay thế đúng FakePublisher bằng ThrowingPublisher cố tình ném lỗi
        DefaultDeliveryUseCase useCase = new DefaultDeliveryUseCase(
                new FakeDeliveryPort(),
                new FakeOutputFileWriter(),
                new ThrowingPublisher(), 
                new FakeRetryPort(),
                repository
        );

        RawEvent rawEvent = new RawEvent();
        rawEvent.setEventId("EVT001");

        TransformedEvent transformed = new TransformedEvent();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("ITEM", "A001");
        transformed.setPayload(payload);

        DeliveryProfile profile = new DeliveryProfile("D:/ignore", "ORACLE_", "csv", ",", true, true, "TRAILER");
        MappingContext mappingContext = new MappingContext();
        mappingContext.setDeliveryProfile(profile);

        PipelineContext context = new PipelineContext();
        context.setRawEvent(rawEvent);
        context.setTransformedEvent(transformed);
        context.setMappingContext(mappingContext);

        // --- 2. ACT ---
        // Sử dụng assertThrows để bắt ngoại lệ ném ra từ hệ thống bọc IO
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.deliver(context)
        );

        // --- 3. ASSERT (5 Tiêu chí xác thực Failure của Tech Lead) ---
        
        // ① Kiểm tra thông báo lỗi Exception có chuẩn xác không
        assertEquals("Publish failed", exception.getMessage());

        // ② Kiểm tra xem có đúng duy nhất 1 bản ghi lịch sử dữ liệu được lưu lại
        assertEquals(1, repository.findAll().size());

        // Rút bản ghi ra để thực hiện kiểm tra trạng thái lỗi nghiệp vụ
        DeliveryArtifact artifact = repository.findAll().get(0);

        // ③ QUAN TRỌNG NHẤT: Trạng thái của Artifact bắt buộc phải chuyển sang FAILED để đánh dấu phục vụ retry sau này
        assertEquals(ArtifactStatus.FAILED, artifact.getStatus());

        // ④ Định danh sự kiện phải được bảo toàn
        assertEquals("EVT001", artifact.getEventId());

        // ⑤ Chứng minh file đã được tạo ra trong bộ nhớ thành công trước khi bị chết ở bước Publish
        assertEquals("ORACLE_TEST.csv", artifact.getFileName());
    }

    // --- CÁC LỚP FAKE OBJECTS ĐƠN NHIỆM ---

    private static class FakeDeliveryPort implements DeliveryPort {
        @Override
        public OutputFile build(TransformedEvent event, DeliveryProfile profile) {
            return new OutputFile("ORACLE_TEST.csv", List.of("HEADER", "ROW", "TRAILER"));
        }
    }

    private static class FakeOutputFileWriter implements OutputFileWriter {
        @Override
        public WorkspaceArtifact write(OutputFile outputFile) {
            return new WorkspaceArtifact(outputFile.fileName(), "workspace/ORACLE_TEST.csv");
        }
    }

    private static class FakePublisher implements OutputPublisher {
        @Override
        public void publish(WorkspaceArtifact workspaceArtifact, DeliveryProfile profile) {
        }
    }

    // HẠ TẦNG FAKE LỖI: Cố tình ném lỗi IO/Runtime để kích hoạt luồng cứu hộ của UseCase
    private static class ThrowingPublisher implements OutputPublisher {
        @Override
        public void publish(WorkspaceArtifact workspaceArtifact, DeliveryProfile profile) {
            throw new RuntimeException("Publish failed");
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
            // ĐÃ SỬA THEO YÊU CẦU: Mỗi event chỉ được save() một lần theo kết quả cuối cùng.
            // Do đó chúng ta clear cái cũ đi (nếu có) trước khi ghi đè kết quả cuối cùng.
            this.artifacts.clear(); 
            this.artifacts.add(artifact);
        }

        @Override
        public List<DeliveryArtifact> findAll() {
            return artifacts;
        }
    }
}
