package com.example.middleware.feature.orchestration.application;

import com.example.middleware.feature.orchestration.application.lifecycle.ExecutionLifecycleManager;
import com.example.middleware.feature.orchestration.application.stage.PipelineStage;
import com.example.middleware.feature.orchestration.domain.Execution;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class Pipeline {

    private final List<PipelineStage> stages;
private final ExecutionLifecycleManager lifecycleManager;
   
    public Pipeline(
        List<PipelineStage> stages,
        ExecutionLifecycleManager lifecycleManager) {

    this.stages = stages;
    this.lifecycleManager = lifecycleManager;
}
    public StageResult execute(PipelineContext context) {

        Execution execution = context.getExecution();

        if (execution != null) {
            lifecycleManager.start(execution);
        }

      for (PipelineStage stage : stages) {

    

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