CREATE TABLE tables (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    name VARCHAR(255) NOT NULL,
    capacity INTEGER,
    location VARCHAR(255),
    status VARCHAR(50)
);

CREATE TABLE reservation (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    session_id UUID,
    reservation_time TIMESTAMP NOT NULL,
    reservation_end_time TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    bill_amount DECIMAL(19, 2),
    table_id UUID NOT NULL,
    CONSTRAINT fk_reservation_table FOREIGN KEY (table_id) REFERENCES tables(id)
);

CREATE TABLE reservation_orders (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    last_updated_date_time TIMESTAMP,
    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    menu_item_id UUID,
    menu_item_name VARCHAR(255),
    price DECIMAL(19, 2),
    quantity INTEGER,
    reservation_id UUID NOT NULL,
    CONSTRAINT fk_reservation_orders_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);
