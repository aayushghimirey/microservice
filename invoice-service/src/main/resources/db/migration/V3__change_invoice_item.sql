ALTER TABLE invoice_item
  ADD COLUMN name VARCHAR(255),
  ADD COLUMN price DECIMAL(10, 2);

ALTER TABLE invoice
  ADD COLUMN payment_method VARCHAR(255);