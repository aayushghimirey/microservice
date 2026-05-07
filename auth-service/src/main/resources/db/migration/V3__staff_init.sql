
CREATE TABLE staff (
    id UUID PRIMARY KEY,

    name VARCHAR(255),
    address VARCHAR(255),
    role VARCHAR(255),
    contact_number VARCHAR(50),

    permissions TEXT[],

    created_date_time TIMESTAMP WITH TIME ZONE,
    last_updated_date_time TIMESTAMP WITH TIME ZONE,

    tenant_id UUID NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

ALTER TABLE staff ENABLE ROW LEVEL SECURITY;


CREATE POLICY staff_tenant_policy ON staff
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);
