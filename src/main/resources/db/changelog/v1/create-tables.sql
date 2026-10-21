CREATE TABLE IF NOT EXISTS units (
    id SERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS professions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tmc_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE CHECK (name IN ('SIZ', 'TOOL', 'EQUIPMENT')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tmc_items (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    type_id INTEGER NOT NULL REFERENCES tmc_types (id) ON DELETE RESTRICT,
    unit_id INTEGER NOT NULL REFERENCES units (id) ON DELETE RESTRICT,
    service_life_months INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS siz_attributes (
    tmc_id INTEGER PRIMARY KEY REFERENCES tmc_items (id) ON DELETE CASCADE,
    size VARCHAR(20),
    wear_period_months INTEGER,
    protection_class VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tool_attributes (
    tmc_id INTEGER PRIMARY KEY REFERENCES tmc_items (id) ON DELETE CASCADE,
    material VARCHAR(100),
    gost_number VARCHAR(50),
    measurement_range VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS equipment_attributes (
    tmc_id INTEGER PRIMARY KEY REFERENCES tmc_items (id) ON DELETE CASCADE,
    drawing_number VARCHAR(50),
    max_cycles INTEGER,
    machine_model VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS norms (
    id SERIAL PRIMARY KEY,
    tmc_id INTEGER NOT NULL REFERENCES tmc_items (id) ON DELETE CASCADE,
    profession_id INTEGER NOT NULL REFERENCES professions (id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    period_months INTEGER NOT NULL CHECK (period_months > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (tmc_id, profession_id)
);

CREATE TABLE IF NOT EXISTS employees (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL,
    hire_date DATE NOT NULL,
    termination_date DATE,
    profession_id INTEGER NOT NULL REFERENCES professions (id) ON DELETE RESTRICT,
    department_id INTEGER NOT NULL REFERENCES departments (id) ON DELETE RESTRICT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    employee_id INTEGER UNIQUE REFERENCES employees (id) ON DELETE CASCADE,
    role_id INTEGER NOT NULL REFERENCES user_roles (id) ON DELETE RESTRICT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS staffing_plans (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employees (id) ON DELETE CASCADE,
    action_type VARCHAR(20) NOT NULL CHECK (action_type IN ('HIRE', 'TERMINATE', 'TRANSFER')),
    effective_date DATE NOT NULL,
    new_profession_id INTEGER REFERENCES professions (id) ON DELETE RESTRICT,
    new_department_id INTEGER REFERENCES departments (id) ON DELETE RESTRICT,
    old_profession_id INTEGER REFERENCES professions (id) ON DELETE RESTRICT,
    old_department_id INTEGER REFERENCES departments (id) ON DELETE RESTRICT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS issued_items (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employees (id) ON DELETE CASCADE,
    tmc_item_id INTEGER NOT NULL REFERENCES tmc_items (id) ON DELETE RESTRICT,
    norm_id INTEGER REFERENCES norms (id) ON DELETE SET NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    issue_date DATE NOT NULL,
    expected_return_date DATE NOT NULL,
    actual_return_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ISSUED' CHECK (status IN ('ISSUED', 'RETURNED', 'WRITTEN_OFF')),
    created_by INTEGER REFERENCES users (id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS calculation_results (
    id SERIAL PRIMARY KEY,
    tmc_id INTEGER NOT NULL REFERENCES tmc_items (id) ON DELETE CASCADE,
    required_quantity INTEGER NOT NULL CHECK (required_quantity >= 0),
    period_month INTEGER NOT NULL CHECK (period_month >= 1 AND period_month <= 12),
    period_year INTEGER NOT NULL CHECK (period_year >= 2020),
    source VARCHAR(20) NOT NULL DEFAULT 'NORM_BASE'
    CHECK (source IN ('NORM_BASE', 'HIRE', 'REPLACEMENT', 'MANUAL')),
    calculation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (tmc_id, period_year, period_month, source)
);

CREATE INDEX IF NOT EXISTS idx_employees_profession ON employees (profession_id);
CREATE INDEX IF NOT EXISTS idx_employees_department ON employees (department_id);
CREATE INDEX IF NOT EXISTS idx_employees_termination ON employees (termination_date);

CREATE INDEX IF NOT EXISTS idx_tmc_items_type ON tmc_items (type_id);
CREATE INDEX IF NOT EXISTS idx_tmc_items_unit ON tmc_items (unit_id);

CREATE INDEX IF NOT EXISTS idx_norms_tmc ON norms (tmc_id);
CREATE INDEX IF NOT EXISTS idx_norms_profession ON norms (profession_id);

CREATE INDEX IF NOT EXISTS idx_staffing_plans_employee ON staffing_plans (employee_id);
CREATE INDEX IF NOT EXISTS idx_staffing_plans_date ON staffing_plans (effective_date);

CREATE INDEX IF NOT EXISTS idx_calculation_results_tmc ON calculation_results (tmc_id);
CREATE INDEX IF NOT EXISTS idx_calculation_results_period ON calculation_results (period_year, period_month);
CREATE INDEX IF NOT EXISTS idx_calculation_results_source ON calculation_results (source);

CREATE INDEX IF NOT EXISTS idx_users_employee ON users (employee_id);
CREATE INDEX IF NOT EXISTS idx_users_role ON users (role_id);

CREATE INDEX IF NOT EXISTS idx_issued_items_employee ON issued_items (employee_id);
CREATE INDEX IF NOT EXISTS idx_issued_items_tmc ON issued_items (tmc_item_id);
CREATE INDEX IF NOT EXISTS idx_issued_items_status ON issued_items (status);
CREATE INDEX IF NOT EXISTS idx_issued_items_expected_date ON issued_items (expected_return_date);
CREATE INDEX IF NOT EXISTS idx_issued_items_norm ON issued_items (norm_id);
CREATE INDEX IF NOT EXISTS idx_issued_items_open ON issued_items (tmc_item_id, status) WHERE status = 'ISSUED';