
-- 1. CHÈN DANH SÁCH ROLE (Bảng auth.roles)

INSERT INTO auth.roles (role_name, description) VALUES
('ROLE_ADMIN', 'Quản trị viên hệ thống - Toàn quyền quản lý'),
('ROLE_PRODUCT_MANAGER', 'Quản lý sản phẩm, danh mục và giá cả'),
('ROLE_WAREHOUSE_STAFF', 'Nhân viên quản lý kho và kiểm kê'),
('ROLE_ORDER_STAFF', 'Nhân viên xử lý đơn hàng và đổi trả'),
('ROLE_CONTENT_STAFF', 'Nhân viên quản lý nội dung, bài viết, banner'),
('ROLE_CUSTOMER', 'Khách hàng đã đăng ký tài khoản'),
('ROLE_GUEST', 'Khách truy cập vãng lai chưa đăng nhập');


-- 2. CHÈN DANH SÁCH PERMISSION (Bảng auth.permissions)

INSERT INTO auth.permissions (permission_name, permission_code, description) VALUES
-- Admin Permissions
('USER_MANAGEMENT', 1001, 'User Management'),
('ROLE_PERMISSION_MANAGEMENT', 1002, 'Role & Permission Management'),
('PRODUCT_MANAGEMENT', 1003, 'Product Management'),
('INVENTORY_MANAGEMENT', 1004, 'Inventory Management'),
('ORDER_MANAGEMENT', 1005, 'Order Management'),
('REVIEW_MANAGEMENT', 1006, 'Review Management'),
('BLOG_MANAGEMENT', 1007, 'Blog Management'),
('BANNER_MANAGEMENT', 1008, 'Banner Management'),
('PROMOTION_MANAGEMENT', 1009, 'Promotion Management'),
('REPORT_MANAGEMENT', 1010, 'Report Management'),
('AUDIT_LOG_MANAGEMENT', 1011, 'Audit Log Management'),

-- Product Manager Permissions
('BRAND_MANAGEMENT', 2001, 'Brand Management'),
('CAR_SERIES_MANAGEMENT', 2002, 'Car Series Management'),
('CATEGORY_MANAGEMENT', 2003, 'Category Management'),
('MANUFACTURER_MANAGEMENT', 2004, 'Manufacturer Management'),
('SCALE_MANAGEMENT', 2005, 'Scale Management'),
('PRODUCT_IMAGE_MANAGEMENT', 2006, 'Product Image Management'),
('PRODUCT_PRICE_MANAGEMENT', 2007, 'Product Price Management'),

-- Warehouse Staff Permissions
('IMPORT_INVENTORY', 3001, 'Import Inventory'),
('EXPORT_INVENTORY', 3002, 'Export Inventory'),
('STOCK_CHECKING', 3003, 'Stock Checking'),
('INVENTORY_ADJUSTMENT', 3004, 'Inventory Adjustment'),
('INVENTORY_VIEW', 3005, 'Inventory View'),

-- Order Staff Permissions
('UPDATE_ORDER_STATUS', 4001, 'Update Order Status'),
('ORDER_CANCELLATION', 4002, 'Order Cancellation'),
('RETURN_REFUND_PROCESSING', 4003, 'Return / Refund Processing'),

-- Content Staff Permissions
('REVIEW_MODERATION', 5001, 'Review Moderation'),

-- Customer & Guest Shared/Specific Permissions
('BROWSE_PRODUCTS', 6001, 'Browse Products'),
('SEARCH_PRODUCTS', 6002, 'Search Products'),
('FILTER_PRODUCTS', 6003, 'Filter Products'),
('VIEW_PRODUCT_DETAILS', 6004, 'View Product Details'),
('CART_MANAGEMENT', 6005, 'Cart'),
('WISHLIST_MANAGEMENT', 6006, 'Wishlist'),
('CHECKOUT', 6007, 'Checkout'),
('ORDER_HISTORY', 6008, 'Order History'),
('ORDER_TRACKING', 6009, 'Order Tracking'),
('REVIEW_PURCHASED_PRODUCTS', 6010, 'Review Purchased Products'),
('MANAGE_PROFILE', 6011, 'Manage Profile'),
('MANAGE_SHIPPING_ADDRESS', 6012, 'Manage Shipping Address'),
('ADD_TO_CART', 6013, 'Add Products to Cart'),
('REGISTER', 6014, 'Register'),
('LOGIN', 6015, 'Login');


-- 3. GÁN PERMISSION CHO TỪNG ROLE (Bảng auth.role_permissions)

-- 3.1. ADMIN
INSERT INTO auth.role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM auth.roles r
CROSS JOIN auth.permissions p
WHERE r.role_name = 'ROLE_ADMIN'
  AND p.permission_name IN (
    'USER_MANAGEMENT', 'ROLE_PERMISSION_MANAGEMENT', 'PRODUCT_MANAGEMENT',
    'INVENTORY_MANAGEMENT', 'ORDER_MANAGEMENT', 'REVIEW_MANAGEMENT',
    'BLOG_MANAGEMENT', 'BANNER_MANAGEMENT', 'PROMOTION_MANAGEMENT',
    'REPORT_MANAGEMENT', 'AUDIT_LOG_MANAGEMENT'
  );

-- 3.2. PRODUCT MANAGER
INSERT INTO auth.role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM auth.roles r
CROSS JOIN auth.permissions p
WHERE r.role_name = 'ROLE_PRODUCT_MANAGER'
  AND p.permission_name IN (
    'PRODUCT_MANAGEMENT', 'BRAND_MANAGEMENT', 'CAR_SERIES_MANAGEMENT',
    'CATEGORY_MANAGEMENT', 'MANUFACTURER_MANAGEMENT', 'SCALE_MANAGEMENT',
    'PRODUCT_IMAGE_MANAGEMENT', 'PRODUCT_PRICE_MANAGEMENT'
  );

-- 3.3. WAREHOUSE STAFF
INSERT INTO auth.role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM auth.roles r
CROSS JOIN auth.permissions p
WHERE r.role_name = 'ROLE_WAREHOUSE_STAFF'
  AND p.permission_name IN (
    'IMPORT_INVENTORY', 'EXPORT_INVENTORY', 'STOCK_CHECKING',
    'INVENTORY_ADJUSTMENT', 'INVENTORY_VIEW'
  );

-- 3.4. ORDER STAFF
INSERT INTO auth.role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM auth.roles r
CROSS JOIN auth.permissions p
WHERE r.role_name = 'ROLE_ORDER_STAFF'
  AND p.permission_name IN (
    'ORDER_MANAGEMENT', 'UPDATE_ORDER_STATUS', 'ORDER_CANCELLATION',
    'RETURN_REFUND_PROCESSING'
  );

-- 3.5. CONTENT STAFF
INSERT INTO auth.role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM auth.roles r
CROSS JOIN auth.permissions p
WHERE r.role_name = 'ROLE_CONTENT_STAFF'
  AND p.permission_name IN (
    'BLOG_MANAGEMENT', 'BANNER_MANAGEMENT', 'REVIEW_MODERATION'
  );

-- 3.6. CUSTOMER (USER)
INSERT INTO auth.role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM auth.roles r
CROSS JOIN auth.permissions p
WHERE r.role_name = 'ROLE_CUSTOMER'
  AND p.permission_name IN (
    'BROWSE_PRODUCTS', 'SEARCH_PRODUCTS', 'FILTER_PRODUCTS', 'VIEW_PRODUCT_DETAILS',
    'CART_MANAGEMENT', 'WISHLIST_MANAGEMENT', 'CHECKOUT', 'ORDER_HISTORY',
    'ORDER_TRACKING', 'REVIEW_PURCHASED_PRODUCTS', 'MANAGE_PROFILE', 'MANAGE_SHIPPING_ADDRESS'
  );

-- 3.7. GUEST
INSERT INTO auth.role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM auth.roles r
CROSS JOIN auth.permissions p
WHERE r.role_name = 'ROLE_GUEST'
  AND p.permission_name IN (
    'BROWSE_PRODUCTS', 'SEARCH_PRODUCTS', 'FILTER_PRODUCTS', 'VIEW_PRODUCT_DETAILS',
    'ADD_TO_CART', 'REGISTER', 'LOGIN'
  );