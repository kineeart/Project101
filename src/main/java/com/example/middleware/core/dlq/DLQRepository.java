package com.example.middleware.core.dlq;

import java.util.ArrayList;
import java.util.List;

public class DLQRepository {

    private final List<DeadLetterEvent> store = new ArrayList<>();

    public void save(DeadLetterEvent event) {
        store.add(event);
    }

    public List<DeadLetterEvent> findAll() {
        return store;
    }
}