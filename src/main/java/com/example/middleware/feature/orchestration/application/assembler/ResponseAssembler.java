package com.example.middleware.feature.orchestration.application.assembler;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import org.springframework.http.ResponseEntity;

public interface ResponseAssembler {

    ResponseEntity<?> success(PipelineContext context);

}