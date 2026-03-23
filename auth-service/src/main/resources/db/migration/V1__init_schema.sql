CREATE TABLE tenant (
    id UUID PRIMARY KEY,
    name VARCHAR(250) NOT NULL UNIQUE,
    contact_email VARCHAR(150),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

ALTER TABLE tenant ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_rls_policy ON tenant
    USING (id = current_setting('app.id')::UUID);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    tenant_id UUID,
    last_update_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    username VARCHAR(250) NOT NULL,
    email VARCHAR(150) UNIQUE,
    phone_number VARCHAR(150) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    permission VARCHAR(255)
);
