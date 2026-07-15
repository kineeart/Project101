package com.example.middleware.feature.transformation.application;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.transformation.domain.TransformationStep;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TransformationRuntime {

    private final List<TransformationStep> steps;

    // SỬA ĐỔI: Ép sắp xếp tường minh qua ArrayList để tránh lỗi không ăn thứ tự order
    public TransformationRuntime(List<TransformationStep> steps) {
        if (steps != null) {
            List<TransformationStep> sortedSteps = new ArrayList<>(steps);
            sortedSteps.sort(Comparator.comparingInt(TransformationStep::order));
            this.steps = sortedSteps;
        } else {
            this.steps = List.of();
        }
        
        // Log ra màn hình để kiểm tra xem thứ tự các bước nạp vào đã chuẩn chưa
        System.out.println("=== TRANSFORMATION PIPELINE INITIALIZED ===");
        for (TransformationStep step : this.steps) {
            System.out.println("Step: " + step.getClass().getSimpleName() + " [Order: " + step.order() + "]");
        }
        System.out.println("===========================================");
    }

    public Object transform(FieldRule rule, Object value) {
        Object result = value;

        for (TransformationStep step : steps) {
            if (!step.supports(rule)) {
                continue;
            }
            System.out.println(
        step.getClass().getSimpleName()
        + " BEFORE = " + result
);
            result = step.transform(rule, result);
            System.out.println(
        step.getClass().getSimpleName()
        + " AFTER = " + result
);
        }

        return result;
    }
}
