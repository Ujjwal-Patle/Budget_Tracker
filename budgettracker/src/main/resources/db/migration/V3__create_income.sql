CREATE TABLE incomes (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(100) NOT NULL,
    income_source VARCHAR(50) NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    income_year INTEGER NOT NULL,
    income_month INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'EXPECTED',
    expected_date DATE,
    received_date DATE,
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_incomes_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_income_amount
        CHECK (amount > 0),

    CONSTRAINT chk_income_year
        CHECK (income_year BETWEEN 2000 AND 2100),

    CONSTRAINT chk_income_month
        CHECK (income_month BETWEEN 1 AND 12),

    CONSTRAINT chk_income_status
        CHECK (status IN ('EXPECTED', 'RECEIVED')),

    CONSTRAINT chk_income_received_date
        CHECK (
            (status = 'EXPECTED' AND received_date IS NULL)
            OR
            (status = 'RECEIVED' AND received_date IS NOT NULL)
        ),

    CONSTRAINT chk_income_title
        CHECK (LENGTH(TRIM(title)) > 0)
);

CREATE INDEX idx_incomes_user_month
    ON incomes(user_id, income_year, income_month);
