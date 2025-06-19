CREATE TABLE products (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price DECIMAL(10, 2) NOT NULL,
                          category VARCHAR(100),
                          stock INT,
                          created_date TIMESTAMP,
                          last_updated_date TIMESTAMP
);

CREATE INDEX idx_products_category ON products(category);