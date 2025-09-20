-- NutriLife Database Setup Script

-- Create databases
CREATE DATABASE nutrilife_auth;
CREATE DATABASE nutrilife_core;

-- Create user for applications
CREATE USER nutrilife_user WITH PASSWORD 'nutrilife_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE nutrilife_auth TO nutrilife_user;
GRANT ALL PRIVILEGES ON DATABASE nutrilife_core TO nutrilife_user;

-- Connect to auth database
\c nutrilife_auth;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_enabled BOOLEAN DEFAULT TRUE,
    is_account_non_locked BOOLEAN DEFAULT TRUE
);

-- Create refresh_tokens table
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_revoked BOOLEAN DEFAULT FALSE
);

-- Create indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);

-- Connect to core database
\c nutrilife_core;

-- Create foods table
CREATE TABLE IF NOT EXISTS foods (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    calories DOUBLE PRECISION NOT NULL,
    protein DOUBLE PRECISION,
    carbohydrates DOUBLE PRECISION,
    fat DOUBLE PRECISION,
    fiber DOUBLE PRECISION,
    source VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create meals table
CREATE TABLE IF NOT EXISTS meals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    meal_type VARCHAR(20) NOT NULL,
    meal_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create meal_items table
CREATE TABLE IF NOT EXISTS meal_items (
    id BIGSERIAL PRIMARY KEY,
    meal_id BIGINT NOT NULL REFERENCES meals(id) ON DELETE CASCADE,
    food_id BIGINT NOT NULL REFERENCES foods(id) ON DELETE CASCADE,
    quantity DOUBLE PRECISION NOT NULL,
    serving_unit VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_foods_name ON foods(name);
CREATE INDEX idx_foods_source ON foods(source);
CREATE INDEX idx_foods_calories ON foods(calories);
CREATE INDEX idx_meals_user_id ON meals(user_id);
CREATE INDEX idx_meals_date ON meals(meal_date);
CREATE INDEX idx_meal_items_meal_id ON meal_items(meal_id);
CREATE INDEX idx_meal_items_food_id ON meal_items(food_id);

-- Insert sample data
INSERT INTO foods (name, description, calories, protein, carbohydrates, fat, fiber, source) VALUES
('Tavuk Göğsü', 'Izgara tavuk göğsü', 165.0, 31.0, 0.0, 3.6, 0.0, 'DATABASE'),
('Pirinç', 'Beyaz pirinç', 130.0, 2.7, 28.0, 0.3, 0.4, 'DATABASE'),
('Brokoli', 'Haşlanmış brokoli', 34.0, 2.8, 6.6, 0.4, 2.6, 'DATABASE'),
('Yumurta', 'Haşlanmış yumurta', 155.0, 13.0, 1.1, 11.0, 0.0, 'DATABASE'),
('Elma', 'Taze elma', 52.0, 0.3, 14.0, 0.2, 2.4, 'DATABASE');

-- Grant privileges on tables
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO nutrilife_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO nutrilife_user;

COMMIT;
