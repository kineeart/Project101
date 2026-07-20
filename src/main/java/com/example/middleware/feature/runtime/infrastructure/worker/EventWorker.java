package com.example.middleware.feature.runtime.infrastructure.worker;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.middleware.feature.runtime.application.usecase.PollBatchUseCase;

@Component
public class EventWorker {

    private final PollBatchUseCase pollBatchUseCase;

    public EventWorker(PollBatchUseCase pollBatchUseCase) {
        this.pollBatchUseCase = pollBatchUseCase;
    }

    // Cứ mỗi 1000ms (1 giây), Spring sẽ tự động kích hoạt hàm này chạy ngầm
    @Scheduled(fixedDelay = 1000)
    public void poll() {
        pollBatchUseCase.poll();
    }
}
