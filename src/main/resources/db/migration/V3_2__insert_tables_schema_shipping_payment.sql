-- Tạo các Schema
CREATE SCHEMA IF NOT EXISTS payment;
CREATE SCHEMA IF NOT EXISTS shipping;

-- ==========================================
-- SCHEMA: payment
-- ==========================================

-- 1. Table: payment.payment_methods
CREATE TABLE payment.payment_methods (
    method_id BIGSERIAL PRIMARY KEY,
    method_name VARCHAR(255) NOT NULL
);

-- 2. Table: payment.payments (One-to-One với sales.orders)
CREATE TABLE payment.payments (
    payment_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT UNIQUE,
    method_id BIGINT,
    transaction_id VARCHAR(255),
    amount NUMERIC(19, 2),
    status VARCHAR(255),
    paid_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES sales.orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_payments_method FOREIGN KEY (method_id) REFERENCES payment.payment_methods(method_id)
);

-- ==========================================
-- SCHEMA: shipping
-- ==========================================

-- 3. Table: shipping.addresses
CREATE TABLE shipping.addresses (
    address_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    province VARCHAR(255),
    district VARCHAR(255),
    ward VARCHAR(255),
    street VARCHAR(255)
);

-- 4. Table: shipping.shipments (One-to-One với sales.orders)
CREATE TABLE shipping.shipments (
    shipment_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT UNIQUE,
    carrier VARCHAR(255),
    tracking_number VARCHAR(255),
    shipping_fee NUMERIC(19, 2),
    status VARCHAR(255),
    estimated_date TIMESTAMP WITHOUT TIME ZONE,
    delivered_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_shipments_order FOREIGN KEY (order_id) REFERENCES sales.orders(order_id) ON DELETE CASCADE
);