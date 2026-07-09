package com.example.middleware.feature.orchestration.application;

import com.example.middleware.feature.orchestration.application.stage.PipelineStage;
import com.example.middleware.feature.orchestration.domain.Execution;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class Pipeline {

    private final List<PipelineStage> stages;

    public Pipeline(List<PipelineStage> stages) {
        this.stages = stages;
    }
    
    public StageResult execute(PipelineContext context) {

        Execution execution = context.getExecution();

        if (execution != null) {
            execution.start();
        }

        for (PipelineStage stage : stages) {

            StageResult result = stage.execute(context);

            if (execution != null) {
                execution.addStep(
                        stage.name(),
                        result,
                        null
                );
            }

            if (result != StageResult.SUCCESS) {

                if (execution != null) {
                    execution.fail();
                }

                return result;
            }
        }

        if (execution != null) {
            execution.complete();
        }

        return StageResult.SUCCESS;
    }
    
}