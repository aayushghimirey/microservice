-- Enable RLS on all tables
ALTER TABLE menu ENABLE ROW LEVEL SECURITY;
ALTER TABLE menu_ingredient ENABLE ROW LEVEL SECURITY;
ALTER TABLE stock_snapshot ENABLE ROW LEVEL SECURITY;
ALTER TABLE variant_snapshot ENABLE ROW LEVEL SECURITY;

-- Create Policies
CREATE POLICY menu_rls_policy ON menu 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

CREATE POLICY menu_ingredient_tenant_policy ON menu_ingredient 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

CREATE POLICY stock_snapshot_tenant_policy ON stock_snapshot 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

CREATE POLICY variant_snapshot_tenant_policy ON variant_snapshot 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);
