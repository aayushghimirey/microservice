-- Enable Row-Level Security
ALTER TABLE tables ENABLE ROW LEVEL SECURITY;
ALTER TABLE reservation ENABLE ROW LEVEL SECURITY;
ALTER TABLE reservation_orders ENABLE ROW LEVEL SECURITY;

-- Force RLS for table owners (optional but usually recommended for multi-tenant apps)
ALTER TABLE tables FORCE ROW LEVEL SECURITY;
ALTER TABLE reservation FORCE ROW LEVEL SECURITY;
ALTER TABLE reservation_orders FORCE ROW LEVEL SECURITY;

-- Create Policies
CREATE POLICY tables_tenant_policy ON tables 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

CREATE POLICY reservation_tenant_policy ON reservation 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

CREATE POLICY reservation_orders_tenant_policy ON reservation_orders 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);
