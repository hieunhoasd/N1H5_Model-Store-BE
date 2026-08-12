-- 1. Bảng permissions
CREATE TABLE IF NOT EXISTS auth.permissions (
    permission_id SERIAL PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL,
    permission_code INT,
    description TEXT
);

-- 2. Bảng roles
CREATE TABLE IF NOT EXISTS auth.roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

-- 3. Bảng users
CREATE TABLE IF NOT EXISTS auth.users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 4. Bảng trung gian Many-to-Many: role_permissions (Gắn Role với Permission)
CREATE TABLE IF NOT EXISTS auth.role_permissions (
    role_id INT REFERENCES auth.roles(role_id) ON DELETE CASCADE,
    permission_id INT REFERENCES auth.permissions(permission_id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- 5. Bảng trung gian Many-to-Many: user_roles (Gắn User với Role)
CREATE TABLE IF NOT EXISTS auth.user_roles (
    user_id INT REFERENCES auth.users(user_id) ON DELETE CASCADE,
    role_id INT REFERENCES auth.roles(role_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);