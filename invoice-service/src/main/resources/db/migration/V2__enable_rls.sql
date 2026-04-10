-- Enable RLS for invoice
ALTER TABLE invoice ENABLE ROW LEVEL SECURITY;
ALTER TABLE invoice FORCE ROW LEVEL SECURITY;

CREATE POLICY invoice_tenant_policy ON invoice
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Enable RLS for invoice_item
ALTER TABLE invoice_item ENABLE ROW LEVEL SECURITY;
ALTER TABLE invoice_item FORCE ROW LEVEL SECURITY;

CREATE POLICY invoice_item_tenant_policy ON invoice_item
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);

-- Enable RLS for invoice_item_ingredient
ALTER TABLE invoice_item_ingredient ENABLE ROW LEVEL SECURITY;
ALTER TABLE invoice_item_ingredient FORCE ROW LEVEL SECURITY;

CREATE POLICY invoice_item_ingredient_tenant_policy ON invoice_item_ingredient
    USING (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid)
    WITH CHECK (tenant_id = NULLIF(current_setting('app.tenant_id', true), '')::uuid);
