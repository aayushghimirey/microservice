CREATE TABLE menu (
    id UUID PRIMARY KEY,

    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
        created_date_time TIMESTAMP,
        last_updated_date_time TIMESTAMP,
        is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
        tenant_id UUID NOT NULL
);

CREATE TABLE menu_ingredient (
    id UUID PRIMARY KEY,

    menu_id UUID NOT NULL REFERENCES menu(id),
    variant_id UUID NOT NULL,
    unit_id UUID NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);

CREATE TABLE stock_snapshot (
    id UUID PRIMARY KEY,
     stock_id UUID,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);

CREATE TABLE variant_snapshot (
    id UUID PRIMARY KEY,
     stock_snapshot_id UUID REFERENCES stock_snapshot(id),
    variant_id UUID,

    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    tenant_id UUID NOT NULL
);
