package com.example.middleware.feature.delivery.infrastructure.writer;

import com.example.middleware.core.dlq.DLQRepository;
import com.example.middleware.core.dlq.DeadLetterEvent;
import com.example.middleware.feature.processing.application.port.RetryDelay;
import com.example.middleware.feature.processing.application.port.RetryPort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RetryableStorage implements RetryPort {

private final DLQRepository dlqRepository;
private final RetryDelay retryDelay;    
private static final int MAX_RETRY = 3;
public RetryableStorage(
        DLQRepository dlqRepository,
        RetryDelay retryDelay) {

    this.dlqRepository = dlqRepository;
    this.retryDelay = retryDelay;
}
@Override
public <T> T execute(
        String eventId,
        Map<String, Object> payload,
        RetryAction<T> action
) {

    int attempt = 0;

    while (attempt < MAX_RETRY) {

        try {

            return action.execute();

        } catch (Exception ex) {

            attempt++;

            System.out.println(
                    "[RETRY] attempt "
                    + attempt
                    + " failed: "
                    + ex.getMessage()
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

            retryDelay.sleep(1000L * attempt);
        }
    }

    throw new RuntimeException(
            "Unexpected failure"
    );
}

}