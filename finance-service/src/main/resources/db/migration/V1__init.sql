CREATE TABLE invoice_record (
    id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL UNIQUE ,
    gross_total NUMERIC NOT NULL DEFAULT 0,
    reservation_start_time TIMESTAMP NOT NULL,
    reservation_end_time TIMESTAMP NOT NULL,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);

CREATE TABLE purchase_record (
    id UUID PRIMARY KEY,
    purchase_id UUID NOT NULL UNIQUE,
    billing_type VARCHAR(50) NOT NULL,
    money_transaction VARCHAR(50) NOT NULL,
    vat_amount NUMERIC DEFAULT 0,
    gross_total NUMERIC NOT NULL,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
)