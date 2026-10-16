
CREATE TABLE expenses (
                          id UUID PRIMARY KEY,
                          user_id UUID NOT NULL,
                          title VARCHAR(100) NOT NULL,
                          category VARCHAR(50) NOT NULL,
                          amount NUMERIC(12, 2) NOT NULL,
                          expense_date DATE NOT NULL,
                          notes VARCHAR(500),
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_expenses_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT chk_expense_amount
                              CHECK (amount > 0),

                          CONSTRAINT chk_expense_title
                              CHECK (LENGTH(TRIM(title)) > 0),

                          CONSTRAINT chk_expense_category
                              CHECK (LENGTH(TRIM(category)) > 0)
);

CREATE INDEX idx_expenses_user_date
    ON expenses(user_id, expense_date);

CREATE INDEX idx_expenses_user_category_date
    ON expenses(user_id, category, expense_date);
