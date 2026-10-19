ALTER TABLE employees ADD COLUMN IF NOT EXISTS termination_date DATE;

CREATE TABLE IF NOT EXISTS issued_items (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employees (id) ON DELETE CASCADE,
    tmc_item_id INTEGER NOT NULL REFERENCES tmc_items (id) ON DELETE RESTRICT,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    issue_date DATE NOT NULL,
    expected_return_date DATE NOT NULL,
    actual_return_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ISSUED' CHECK (status IN ('ISSUED', 'RETURNED', 'WRITTEN_OFF')),
    created_by INTEGER REFERENCES users (id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_issued_items_employee ON issued_items (employee_id);
CREATE INDEX IF NOT EXISTS idx_issued_items_tmc ON issued_items (tmc_item_id);
CREATE INDEX IF NOT EXISTS idx_issued_items_status ON issued_items (status);
CREATE INDEX IF NOT EXISTS idx_issued_items_expected_date ON issued_items (expected_return_date);