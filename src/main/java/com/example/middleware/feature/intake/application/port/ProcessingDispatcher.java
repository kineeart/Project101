package com.example.middleware.feature.intake.application.port;

public interface ProcessingDispatcher {

    String type();

    void dispatch(String eventId);

}