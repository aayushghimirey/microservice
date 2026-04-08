-- Initial Schema for Inventory Service

CREATE TABLE vendor (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    name VARCHAR(255),
    address VARCHAR(255),
    contact_number VARCHAR(255),
    pan_number VARCHAR(255)
);

CREATE TABLE stock (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE stock_variant (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT,
    name VARCHAR(255) NOT NULL,
    base_unit VARCHAR(50) NOT NULL,
    opening_stock DECIMAL(19, 4) NOT NULL DEFAULT 0,
    current_stock DECIMAL(19, 4) NOT NULL DEFAULT 0,
    stock_id UUID NOT NULL REFERENCES stock(id)
);

CREATE TABLE variant_unit (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    name VARCHAR(255) NOT NULL,
    conversion_rate DECIMAL(19, 4) NOT NULL,
    unit_type VARCHAR(50) NOT NULL,
    stock_variant_id UUID NOT NULL REFERENCES stock_variant(id)
);

CREATE TABLE purchase (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    invoice_number VARCHAR(255) NOT NULL,
    billing_type VARCHAR(50) NOT NULL,
    money_transaction VARCHAR(50) NOT NULL,
    discount_amount DECIMAL(19, 4) DEFAULT 0,
    sub_total DECIMAL(19, 4) NOT NULL DEFAULT 0,
    vat_amount DECIMAL(19, 4) DEFAULT 0,
    gross_total DECIMAL(19, 4) DEFAULT 0,
    vendor_id UUID REFERENCES vendor(id)
);

CREATE TABLE purchase_items (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    variant_id UUID NOT NULL,
    unit_id UUID NOT NULL,
    quantity DECIMAL(19, 4) NOT NULL,
    per_unit_price DECIMAL(19, 4) NOT NULL,
    discount_amount DECIMAL(19, 4),
    sub_total DECIMAL(19, 4) NOT NULL,
    net_total DECIMAL(19, 4) NOT NULL,
    purchase_id UUID REFERENCES purchase(id)
);

CREATE TABLE stock_transaction (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    reference_id UUID,
    reference_type VARCHAR(50),
    variant_id UUID NOT NULL,
    unit_id UUID NOT NULL,
    quantity_change DECIMAL(19, 4) NOT NULL,
    balance_after DECIMAL(19, 4) NOT NULL,
    remark TEXT
);
