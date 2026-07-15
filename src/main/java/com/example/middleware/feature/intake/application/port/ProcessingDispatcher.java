package com.example.middleware.feature.intake.application.port;

public interface ProcessingDispatcher {

    void dispatch(String eventId);

}