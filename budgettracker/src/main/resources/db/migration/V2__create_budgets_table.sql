CREATE TABLE budgets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    budget_year INTEGER NOT NULL,
    budget_month INTEGER NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_budgets_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT uq_budgets_user_year_month
        UNIQUE (user_id, budget_year, budget_month),

    CONSTRAINT chk_budgets_year
        CHECK (budget_year BETWEEN 2000 AND 2100),

    CONSTRAINT chk_budgets_month
        CHECK (budget_month BETWEEN 1 AND 12)
);

CREATE INDEX idx_budgets_user_id ON budgets(user_id);