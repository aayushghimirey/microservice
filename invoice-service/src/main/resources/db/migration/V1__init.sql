CREATE TABLE invoice (
    id UUID PRIMARY KEY,
    bill_number VARCHAR(255),
    table_id UUID,
    session_id UUID,
    reservation_id UUID,
    status VARCHAR(50),
    discount_amount NUMERIC(19, 2) DEFAULT 0,
    sub_total NUMERIC(19, 2) DEFAULT 0,
    gross_total NUMERIC(19, 2) DEFAULT 0,
    reservation_time TIMESTAMP,
    reservation_end_time TIMESTAMP,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);

CREATE TABLE invoice_item (
    id UUID PRIMARY KEY,
    menu_item_id UUID,
    quantity DOUBLE PRECISION NOT NULL,
    printable BOOLEAN DEFAULT FALSE,
    invoice_id UUID REFERENCES invoice(id),
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);

CREATE TABLE invoice_item_ingredient (
    id UUID PRIMARY KEY,
    variant_id UUID,
    unit_id UUID,
    quantity DOUBLE PRECISION NOT NULL,
    invoice_item_id UUID REFERENCES invoice_item(id),
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);
