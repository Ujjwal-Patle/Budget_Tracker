
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       currency VARCHAR(3) NOT NULL DEFAULT 'INR',
                       timezone VARCHAR(100) NOT NULL DEFAULT 'Asia/Kolkata',
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
