package com.example.middleware.feature.delivery.infrastructure.storage;

import com.example.middleware.core.dlq.DLQRepository;
import com.example.middleware.core.dlq.DeadLetterEvent;
import com.example.middleware.feature.processing.application.port.RetryPort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RetryableStorage implements RetryPort {

    private final DLQRepository dlqRepository = new DLQRepository();
    private static final int MAX_RETRY = 3;

        public String execute(
            String eventId,
            Map<String, Object> payload,
            RetryAction action
        ) {

        int attempt = 0;

        while (attempt < MAX_RETRY) {
            try {
                return action.execute(); // SUCCESS
            } catch (Exception ex) {

                attempt++;

                System.out.println(
                        "[RETRY] attempt " + attempt +
                                " failed: " + ex.getMessage()
                );

                if (attempt >= MAX_RETRY) {

                    dlqRepository.save(
                            new DeadLetterEvent(
                                    eventId,
                                    ex.getMessage(),
                                    payload,
                                    attempt
                            )
                    );

                    throw new RuntimeException(
                            "Sent to DLQ: " + eventId
                    );
                }

                try {
                    Thread.sleep(1000L * attempt);
                } catch (InterruptedException ignored) {}
            }
        }

        throw new RuntimeException("Unexpected failure");
    }

}