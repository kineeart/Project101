package com.example.middleware.feature.orchestration.application;

import com.example.middleware.feature.orchestration.application.lifecycle.ExecutionLifecycleManager;
import com.example.middleware.feature.orchestration.application.stage.PipelineStage;
import com.example.middleware.feature.orchestration.domain.Execution;
import com.example.middleware.feature.runtime.application.RuntimeStateManager; // Import thêm Manager mới
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Pipeline {

    private final List<PipelineStage> stages;
    private final ExecutionLifecycleManager lifecycleManager;
    private final RuntimeStateManager runtimeStateManager; // Thêm thuộc tính quản lý trạng thái runtime

    // Tích hợp dependency vào Constructor để Spring tự động inject
    public Pipeline(
            List<PipelineStage> stages,
            ExecutionLifecycleManager lifecycleManager,
            RuntimeStateManager runtimeStateManager) {
        this.stages = stages;
        this.lifecycleManager = lifecycleManager;
        this.runtimeStateManager = runtimeStateManager;
    }

    public StageResult execute(PipelineContext context) {

        Execution execution = context.getExecution();

        if (execution != null) {
            lifecycleManager.start(execution);
        }

        for (PipelineStage stage : stages) {

            // Bước tích hợp mới: Cập nhật trạng thái sự kiện sang trạng thái của Stage hiện tại
            runtimeStateManager.enterStage(
                    context.getRawEvent().getEventId(), // Sử dụng eventId làm batchId như thiết kế hiện tại
                    stage.name()
            );

            // Tiến hành thực thi stage
            StageResult result = stage.execute(context);

            if (execution != null) {
                lifecycleManager.stageFinished(
                    execution,
                    stage.name(),
                    result,
                    null
                );
            }

            if (result != StageResult.SUCCESS) {
                if (execution != null) {
                    lifecycleManager.fail(
                        execution,
                        null
                    );
                }
                return result;
            }
        }

        if (execution != null) {
            lifecycleManager.complete(execution);
        }

        return StageResult.SUCCESS;
    }
}
