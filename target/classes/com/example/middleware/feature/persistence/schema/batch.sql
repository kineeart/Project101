CREATE TABLE batch (

    batch_id            VARCHAR(100)    NOT NULL,

    event_id            VARCHAR(100)    NOT NULL,

    version             INTEGER         NOT NULL,

    received_at         TIMESTAMP,

    generated_at        TIMESTAMP,

    total_record        INTEGER,

    valid_record        INTEGER,

    invalid_record      INTEGER,

    written_record      INTEGER,

    retry_count         INTEGER,

    lease_owner         VARCHAR(100),

    lease_until         TIMESTAMP,

    PRIMARY KEY (batch_id),

    FOREIGN KEY (event_id)

        REFERENCES event(event_id)

);