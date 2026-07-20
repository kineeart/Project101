package com.example.middleware.feature.runtime.application.usecase;

import java.util.List;
import com.example.middleware.feature.runtime.application.model.BatchDetail;
import com.example.middleware.feature.runtime.application.model.BatchSummary;

public interface GetBatchDetailUseCase {
    
    BatchDetail get(String batchId);
    
    List<BatchSummary> list(); // Định nghĩa thêm hàm lấy danh sách cho Bước 5
}
