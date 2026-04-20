
CREATE TABLE IF NOT EXISTS outbox_event (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_type  VARCHAR(100)  NOT NULL,
    topic           VARCHAR(255),
    aggregate_id    UUID          NOT NULL,
    event_type      VARCHAR(100)  NOT NULL,
    payload         TEXT,
    processed       BOOLEAN       NOT NULL DEFAULT FALSE,
    tenant_id       UUID          NOT NULL,
    created_at      TIMESTAMP
);

