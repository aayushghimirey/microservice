ALTER TABLE business_detail
ENABLE ROW LEVEL SECURITY;

CREATE POLICY business_detail_isolation ON business_detail
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

