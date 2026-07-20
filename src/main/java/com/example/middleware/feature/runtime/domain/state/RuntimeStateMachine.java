package com.example.middleware.feature.runtime.domain.state;

import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus; // SỬA ĐỒNG BỘ Ở ĐÂY

public interface RuntimeStateMachine {
    void transit(BatchRecord batch, BatchStatus next);
}