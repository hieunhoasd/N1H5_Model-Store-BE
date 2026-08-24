-- Create Schema
CREATE SCHEMA IF NOT EXISTS catalog;

-- 1. Table: catalog.brands
CREATE TABLE catalog.brands (
    brand_id BIGSERIAL PRIMARY KEY,
    brand_name VARCHAR(255) NOT NULL,
    country VARCHAR(255),
    founded_year INTEGER,
    logos VARCHAR(255),
    description TEXT
);

-- 2. Table: catalog.car_series
CREATE TABLE catalog.car_series (
    series_id BIGSERIAL PRIMARY KEY,
    brand_id BIGINT,
    series_name VARCHAR(255) NOT NULL,
    description TEXT,
    CONSTRAINT fk_car_series_brand FOREIGN KEY (brand_id) REFERENCES catalog.brands(brand_id)
);

-- 3. Table: catalog.car_generations
CREATE TABLE catalog.car_generations (
    generation_id BIGSERIAL PRIMARY KEY,
    series_id BIGINT,
    generation_name VARCHAR(255) NOT NULL,
    start_year INTEGER,
    end_year INTEGER,
    CONSTRAINT fk_car_generations_series FOREIGN KEY (series_id) REFERENCES catalog.car_series(series_id)
);

-- 4. Table: catalog.car_variants
CREATE TABLE catalog.car_variants (
    variant_id BIGSERIAL PRIMARY KEY,
    generation_id BIGINT,
    variant_name VARCHAR(255) NOT NULL,
    engine VARCHAR(255),
    horsepower INTEGER,
    CONSTRAINT fk_car_variants_generation FOREIGN KEY (generation_id) REFERENCES catalog.car_generations(generation_id)
);

-- 5. Table: catalog.categories
CREATE TABLE catalog.categories (
    category_id BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    description TEXT
);

-- 6. Table: catalog.manufacturers
CREATE TABLE catalog.manufacturers (
    manufacturer_id BIGSERIAL PRIMARY KEY,
    manufacturer_name VARCHAR(255) NOT NULL,
    country VARCHAR(255),
    logo VARCHAR(255)
);

-- 7. Table: catalog.scales
CREATE TABLE catalog.scales (
    scale_id BIGSERIAL PRIMARY KEY,
    scale_name VARCHAR(255) NOT NULL
);

-- 8. Table: catalog.colors
CREATE TABLE catalog.colors (
    color_id BIGSERIAL PRIMARY KEY,
    color_name VARCHAR(255) NOT NULL,
    hex VARCHAR(255)
);

-- 9. Table: catalog.tags
CREATE TABLE catalog.tags (
    tag_id BIGSERIAL PRIMARY KEY,
    tag_name VARCHAR(255) NOT NULL
);

-- 10. Table: catalog.products
CREATE TABLE catalog.products (
    product_id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(255) NOT NULL UNIQUE,
    product_name VARCHAR(255) NOT NULL,
    brand_id BIGINT,
    category_id BIGINT,
    series_id BIGINT,
    generation_id BIGINT,
    variant_id BIGINT,
    manufacturer_id BIGINT,
    scale_id BIGINT,
    color_id BIGINT,
    price NUMERIC(19, 2),
    stock INTEGER,
    description TEXT,
    status VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_brand FOREIGN KEY (brand_id) REFERENCES catalog.brands(brand_id),
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES catalog.categories(category_id),
    CONSTRAINT fk_products_series FOREIGN KEY (series_id) REFERENCES catalog.car_series(series_id),
    CONSTRAINT fk_products_generation FOREIGN KEY (generation_id) REFERENCES catalog.car_generations(generation_id),
    CONSTRAINT fk_products_variant FOREIGN KEY (variant_id) REFERENCES catalog.car_variants(variant_id),
    CONSTRAINT fk_products_manufacturer FOREIGN KEY (manufacturer_id) REFERENCES catalog.manufacturers(manufacturer_id),
    CONSTRAINT fk_products_scale FOREIGN KEY (scale_id) REFERENCES catalog.scales(scale_id),
    CONSTRAINT fk_products_color FOREIGN KEY (color_id) REFERENCES catalog.colors(color_id)
);

-- 11. Table: catalog.product_images
CREATE TABLE catalog.product_images (
    image_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    image_url VARCHAR(255) NOT NULL,
    alt_text VARCHAR(255),
    display_order INTEGER,
    is_main BOOLEAN,
    CONSTRAINT fk_product_images_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id) ON DELETE CASCADE
);

-- 12. Table: catalog.product_tags (Bảng trung gian cho quan hệ M-M)
CREATE TABLE catalog.product_tags (
    product_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, tag_id),
    CONSTRAINT fk_product_tags_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id) ON DELETE CASCADE,
    CONSTRAINT fk_product_tags_tag FOREIGN KEY (tag_id) REFERENCES catalog.tags(tag_id) ON DELETE CASCADE
);


-- Tạo các Schema nếu chưa tồn tại
CREATE SCHEMA IF NOT EXISTS inventory;
CREATE SCHEMA IF NOT EXISTS sales;

-- ==========================================
-- SCHEMA: inventory
-- ==========================================

-- 1. Table: inventory.warehouses
CREATE TABLE inventory.warehouses (
    warehouse_id BIGSERIAL PRIMARY KEY,
    warehouse_name VARCHAR(255) NOT NULL,
    address TEXT
);

-- 2. Table: inventory.inventories
CREATE TABLE inventory.inventories (
    inventory_id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT,
    product_id BIGINT,
    quantity INTEGER DEFAULT 0,
    reserved_quantity INTEGER DEFAULT 0,
    CONSTRAINT fk_inventories_warehouse FOREIGN KEY (warehouse_id) REFERENCES inventory.warehouses(warehouse_id),
    CONSTRAINT fk_inventories_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id)
);

-- 3. Table: inventory.stock_history
CREATE TABLE inventory.stock_history (
    history_id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT,
    product_id BIGINT,
    user_id BIGINT,
    quantity INTEGER,
    action VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_history_warehouse FOREIGN KEY (warehouse_id) REFERENCES inventory.warehouses(warehouse_id),
    CONSTRAINT fk_stock_history_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id)
);

-- ==========================================
-- SCHEMA: sales
-- ==========================================

-- 4. Table: sales.carts
CREATE TABLE sales.carts (
    cart_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Table: sales.cart_items
CREATE TABLE sales.cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT,
    quantity INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES sales.carts(cart_id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id)
);

-- 6. Table: sales.orders
CREATE TABLE sales.orders (
    order_id BIGSERIAL PRIMARY KEY,
    order_code VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT,
    address_id BIGINT,
    coupon_id BIGINT,
    total NUMERIC(19, 2),
    discount NUMERIC(19, 2),
    shipping_fee NUMERIC(19, 2),
    final_total NUMERIC(19, 2),
    status VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    note TEXT
);

-- 7. Table: sales.order_items
CREATE TABLE sales.order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT,
    price NUMERIC(19, 2),
    quantity INTEGER NOT NULL,
    subtotal NUMERIC(19, 2),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES sales.orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id)
);