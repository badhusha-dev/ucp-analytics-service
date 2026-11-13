-- B-02: analytics projections are per tenant. Orders and returns carry order-service's tenant; carts have no tenant upstream
-- and, like all earlier rows, belong to the legacy tenant.
ALTER TABLE orders ADD COLUMN IF NOT EXISTS tenant_id UUID;
UPDATE orders SET tenant_id = '00000000-0000-4000-8000-000000000001' WHERE tenant_id IS NULL;
ALTER TABLE orders ALTER COLUMN tenant_id SET DEFAULT '00000000-0000-4000-8000-000000000001';
ALTER TABLE orders ALTER COLUMN tenant_id SET NOT NULL;
CREATE INDEX IF NOT EXISTS idx_analytics_orders_tenant_created ON orders (tenant_id, created_at);

ALTER TABLE order_return ADD COLUMN IF NOT EXISTS tenant_id UUID;
UPDATE order_return SET tenant_id = '00000000-0000-4000-8000-000000000001' WHERE tenant_id IS NULL;
ALTER TABLE order_return ALTER COLUMN tenant_id SET DEFAULT '00000000-0000-4000-8000-000000000001';
ALTER TABLE order_return ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE customer_cart ADD COLUMN IF NOT EXISTS tenant_id UUID;
UPDATE customer_cart SET tenant_id = '00000000-0000-4000-8000-000000000001' WHERE tenant_id IS NULL;
ALTER TABLE customer_cart ALTER COLUMN tenant_id SET DEFAULT '00000000-0000-4000-8000-000000000001';
ALTER TABLE customer_cart ALTER COLUMN tenant_id SET NOT NULL;
