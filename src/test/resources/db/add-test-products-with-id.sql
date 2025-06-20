INSERT INTO products (id, name, category, price) VALUES (1, 'Phone', 'Electronics', 699.99);
INSERT INTO products (id, name, category, price) VALUES (2, 'Laptop', 'Electronics', 1200.00);
INSERT INTO products (id, name, category, price) VALUES ( 3, 'Table', 'Furniture', 150.00);

ALTER TABLE products ALTER COLUMN id RESTART WITH 4;
