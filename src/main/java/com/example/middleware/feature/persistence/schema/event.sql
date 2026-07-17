CREATE TABLE event (

    event_id            VARCHAR(100)    NOT NULL,

    batch_id            VARCHAR(100)    NOT NULL,

    profile_id          VARCHAR(100)    NOT NULL,

    source_system       VARCHAR(50),

    event_type          VARCHAR(50),

    status              VARCHAR(30)     NOT NULL,

    owner_instance      VARCHAR(100),

    created_at          TIMESTAMP       NOT NULL,

    updated_at          TIMESTAMP       NOT NULL,

    PRIMARY KEY (event_id)

);