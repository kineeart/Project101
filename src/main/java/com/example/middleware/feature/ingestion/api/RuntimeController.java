package com.example.middleware.feature.ingestion.api;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.middleware.feature.runtime.application.usecase.GetBatchDetailUseCase;
import com.example.middleware.feature.runtime.application.model.BatchDetail;
import com.example.middleware.feature.runtime.application.model.BatchSummary;

@RestController
@RequestMapping("/api/v1/runtime/batches")
public class RuntimeController {

    private final GetBatchDetailUseCase useCase;

    // Inject UseCase vào Controller
    public RuntimeController(GetBatchDetailUseCase useCase) {
        this.useCase = useCase;
    }

    // Bước 5: API lấy danh sách gọn nhẹ cho Dashboard hiển thị
    // URL: GET http://localhost:8080/api/v1/runtime/batches
    @GetMapping
    public List<BatchSummary> list() {
        return useCase.list();
    }

    // Bước 4: API lấy chi tiết kèm dòng lịch sử Timeline của 1 Batch
    // URL: GET http://localhost:8080/api/v1/runtime/batches/{batchId}
    @GetMapping("/{batchId}")
    public BatchDetail detail(@PathVariable String batchId) {
        return useCase.get(batchId);
    }
}
