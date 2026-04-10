-- Enable RLS for invoice_record
ALTER TABLE invoice_record ENABLE ROW LEVEL SECURITY;
ALTER TABLE invoice_record FORCE ROW LEVEL SECURITY;

CREATE POLICY invoice_record_tenant_policy ON invoice_record
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Enable RLS for purchase_record
ALTER TABLE purchase_record ENABLE ROW LEVEL SECURITY;
ALTER TABLE purchase_record FORCE ROW LEVEL SECURITY;

CREATE POLICY purchase_record_tenant_policy ON purchase_record
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);
