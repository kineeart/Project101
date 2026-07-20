package com.example.middleware.feature.runtime.domain.state;

import java.util.Set;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus; // Dòng import quyết định

@Component 
public class DefaultRuntimeStateMachine implements RuntimeStateMachine {

      private final Set<RuntimeTransition> transitions = Set.of(
        new RuntimeTransition(BatchStatus.RECEIVED, BatchStatus.PROCESSING),
        new RuntimeTransition(BatchStatus.PROCESSING, BatchStatus.VALIDATED), // BƯỚC MỚI
        new RuntimeTransition(BatchStatus.VALIDATED, BatchStatus.MAPPED),     // BƯỚC MỚI
        new RuntimeTransition(BatchStatus.MAPPED, BatchStatus.BUILT),         // BƯỚC MỚI
        new RuntimeTransition(BatchStatus.BUILT, BatchStatus.WRITING),         // BƯỚC MỚI
        new RuntimeTransition(BatchStatus.WRITING, BatchStatus.WRITTEN),
        new RuntimeTransition(BatchStatus.WRITING, BatchStatus.PARTIAL),
        
        // Các luồng rẽ nhánh lỗi (Có thể lỗi ở bất kỳ giai đoạn nào)
        new RuntimeTransition(BatchStatus.PROCESSING, BatchStatus.FAILED),
        new RuntimeTransition(BatchStatus.VALIDATED, BatchStatus.FAILED),
        new RuntimeTransition(BatchStatus.MAPPED, BatchStatus.FAILED),
        new RuntimeTransition(BatchStatus.BUILT, BatchStatus.FAILED),
        new RuntimeTransition(BatchStatus.WRITING, BatchStatus.FAILED)
    );


    @Override
    public void transit(BatchRecord batch, BatchStatus next) {
        // batch.getStatus() và next lúc này đã đồng bộ 100% kiểu dữ liệu
        RuntimeTransition transition = new RuntimeTransition(
                batch.getStatus(),
                next
        );

        if (!transitions.contains(transition)) {
            throw new IllegalStateException(
                    "Illegal transition: "
                            + batch.getStatus()
                            + " -> "
                            + next
            );
        }

        batch.changeStatus(next); // Hàm này giờ đã là public nên gọi thoải mái không lo lỗi visible
    }
}
