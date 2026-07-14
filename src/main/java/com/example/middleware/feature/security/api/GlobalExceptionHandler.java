package com.example.middleware.feature.security.api;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.middleware.feature.processing.domain.exception.DuplicateEventException;

@RestControllerAdvice
public class GlobalExceptionHandler {
@ExceptionHandler(Exception.class)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public ErrorResponse handle(Exception ex) {

    return new ErrorResponse(
            "INTERNAL_ERROR",
            ex.getMessage(),
            Instant.now()
    );
}
@ExceptionHandler(DuplicateEventException.class)
@ResponseStatus(HttpStatus.CONFLICT)
public ErrorResponse duplicate(
        DuplicateEventException ex) {

    return new ErrorResponse(
            "DUPLICATE_EVENT",
            ex.getMessage(),
            Instant.now()
    );
}
}
