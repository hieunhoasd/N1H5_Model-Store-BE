-- Tạo các Schema
CREATE SCHEMA IF NOT EXISTS review;
CREATE SCHEMA IF NOT EXISTS promotion;
CREATE SCHEMA IF NOT EXISTS notification;
CREATE SCHEMA IF NOT EXISTS audit;
CREATE SCHEMA IF NOT EXISTS wishlist;
CREATE SCHEMA IF NOT EXISTS loyalty;

-- ==========================================
-- SCHEMA: review
-- ==========================================

-- 1. Table: review.reviews
CREATE TABLE review.reviews (
    review_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    product_id BIGINT,
    rating INTEGER,
    comment TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id)
);

-- 2. Table: review.review_images
CREATE TABLE review.review_images (
    image_id BIGSERIAL PRIMARY KEY,
    review_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    CONSTRAINT fk_review_images_review FOREIGN KEY (review_id) REFERENCES review.reviews(review_id) ON DELETE CASCADE
);

-- ==========================================
-- SCHEMA: promotion
-- ==========================================

-- 3. Table: promotion.coupons
CREATE TABLE promotion.coupons (
    coupon_id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    discount NUMERIC(19, 2),
    quantity INTEGER,
    status VARCHAR(255),
    start_date TIMESTAMP WITHOUT TIME ZONE,
    expired_at TIMESTAMP WITHOUT TIME ZONE,
    point_cost INTEGER
);

-- 4. Table: promotion.product_discounts
CREATE TABLE promotion.product_discounts (
    discount_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT,
    discount_percent NUMERIC(19, 2),
    status VARCHAR(255),
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_product_discounts_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id)
);

-- ==========================================
-- SCHEMA: notification
-- ==========================================

-- 5. Table: notification.notifications
CREATE TABLE notification.notifications (
    notification_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    title VARCHAR(255),
    content TEXT,
    read_status BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- SCHEMA: audit
-- ==========================================

-- 6. Table: audit.audit_logs
CREATE TABLE audit.audit_logs (
    log_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(255),
    entity_name VARCHAR(255),
    entity_id BIGINT,
    old_value TEXT,
    new_value TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- SCHEMA: wishlist
-- ==========================================

-- 7. Table: wishlist.wishlist
CREATE TABLE wishlist.wishlist (
    wishlist_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    name VARCHAR(255),
    description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 8. Table: wishlist.wishlist_items
CREATE TABLE wishlist.wishlist_items (
    wishlist_item_id BIGSERIAL PRIMARY KEY,
    wishlist_id BIGINT NOT NULL,
    product_id BIGINT,
    added_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_items_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlist.wishlist(wishlist_id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_items_product FOREIGN KEY (product_id) REFERENCES catalog.products(product_id)
);

-- ==========================================
-- SCHEMA: loyalty
-- ==========================================

-- 9. Table: loyalty.user_points
CREATE TABLE loyalty.user_points (
    point_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    current_point INTEGER DEFAULT 0,
    lifetime_point INTEGER DEFAULT 0,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 10. Table: loyalty.point_history
CREATE TABLE loyalty.point_history (
    history_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    order_id BIGINT,
    review_id BIGINT,
    point INTEGER,
    type VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_point_history_order FOREIGN KEY (order_id) REFERENCES sales.orders(order_id) ON DELETE SET NULL,
    CONSTRAINT fk_point_history_review FOREIGN KEY (review_id) REFERENCES review.reviews(review_id) ON DELETE SET NULL
);

-- 11. Table: loyalty.point_rules
CREATE TABLE loyalty.point_rules (
    rule_id BIGSERIAL PRIMARY KEY,
    action VARCHAR(255),
    point INTEGER,
    status VARCHAR(255)
);

-- 12. Table: loyalty.coupon_redemption
CREATE TABLE loyalty.coupon_redemption (
    redemption_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    coupon_id BIGINT,
    point_used INTEGER,
    redeemed_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_coupon_redemption_coupon FOREIGN KEY (coupon_id) REFERENCES promotion.coupons(coupon_id)
);

