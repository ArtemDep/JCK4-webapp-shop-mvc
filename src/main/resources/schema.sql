-- PostgreSQL database schema. The application uses UTF-8 for Russian text.

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    total_price DECIMAL(10,2) NOT NULL CHECK (total_price >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'CREATED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);

-- BCrypt hash for the demo administrator password: admin
INSERT INTO users (username, password, email, role)
VALUES ('admin', '$2a$12$OOkrnYwKyZ5cN7B9iFjRK.0.TEO4ThCtL/2nDu7ZlgQMurDXi6K8q', 'admin@example.com', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

INSERT INTO products (name, description, price, quantity)
VALUES
    ('Ноутбук Lenovo', 'Ноутбук для работы и учебы', 75000.00, 10),
    ('Мышь Logitech', 'Беспроводная мышь', 2500.00, 25),
    ('Клавиатура', 'Механическая клавиатура', 5000.00, 15)
ON CONFLICT DO NOTHING;
