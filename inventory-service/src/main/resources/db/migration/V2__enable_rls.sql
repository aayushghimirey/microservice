-- Enable RLS on all tables and create policies

-- Purchase table
ALTER TABLE purchase ENABLE ROW LEVEL SECURITY;
ALTER TABLE purchase FORCE ROW LEVEL SECURITY;
CREATE POLICY purchase_tenant_policy ON purchase 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Purchase Items table
ALTER TABLE purchase_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE purchase_items FORCE ROW LEVEL SECURITY;
CREATE POLICY purchase_items_tenant_policy ON purchase_items 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Stock table
ALTER TABLE stock ENABLE ROW LEVEL SECURITY;
ALTER TABLE stock FORCE ROW LEVEL SECURITY;
CREATE POLICY stock_tenant_policy ON stock 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Stock Transaction table
ALTER TABLE stock_transaction ENABLE ROW LEVEL SECURITY;
ALTER TABLE stock_transaction FORCE ROW LEVEL SECURITY;
CREATE POLICY stock_transaction_tenant_policy ON stock_transaction 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Stock Variant table
ALTER TABLE stock_variant ENABLE ROW LEVEL SECURITY;
ALTER TABLE stock_variant FORCE ROW LEVEL SECURITY;
CREATE POLICY stock_variant_tenant_policy ON stock_variant 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Variant Unit table
ALTER TABLE variant_unit ENABLE ROW LEVEL SECURITY;
ALTER TABLE variant_unit FORCE ROW LEVEL SECURITY;
CREATE POLICY variant_unit_tenant_policy ON variant_unit 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Vendor table
ALTER TABLE vendor ENABLE ROW LEVEL SECURITY;
ALTER TABLE vendor FORCE ROW LEVEL SECURITY;
CREATE POLICY vendor_tenant_policy ON vendor 
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);
