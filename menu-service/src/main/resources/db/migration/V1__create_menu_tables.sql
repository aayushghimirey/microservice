CREATE TABLE menu (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE menu_ingredient (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    menu_id UUID NOT NULL REFERENCES menu(id),
    variant_id UUID NOT NULL,
    unit_id UUID NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE stock_snapshot (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    stock_id UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE variant_snapshot (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    stock_snapshot_id UUID REFERENCES stock_snapshot(id),
    variant_id UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);
