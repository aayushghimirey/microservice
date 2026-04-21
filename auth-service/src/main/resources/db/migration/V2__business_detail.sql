CREATE TABLE business_detail (
    id UUID PRIMARY KEY,
    created_date_time TIMESTAMP,
    tenant_id UUID,
    last_updated_date_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    company_name VARCHAR(250) NOT NULL,
    business_email VARCHAR(150) UNIQUE,
     address VARCHAR(255) NOT NULL,
    business_number VARCHAR(100) UNIQUE NOT NULL,
    pan_number VARCHAR(100) UNIQUE  NOT NULL

);

ALTER TABLE business_detail
ENABLE ROW LEVEL SECURITY;

CREATE POLICY business_detail_isolation ON business_detail
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

