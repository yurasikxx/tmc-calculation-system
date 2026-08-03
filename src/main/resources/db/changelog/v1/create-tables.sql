-- Справочник подразделений
CREATE TABLE IF NOT EXISTS departments (
                                           id SERIAL PRIMARY KEY,
                                           name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Справочник профессий
CREATE TABLE IF NOT EXISTS professions (
                                           id SERIAL PRIMARY KEY,
                                           name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Справочник сотрудников
CREATE TABLE IF NOT EXISTS employees (
                                         id SERIAL PRIMARY KEY,
                                         full_name VARCHAR(200) NOT NULL,
    hire_date DATE NOT NULL,
    termination_date DATE NULL,
    profession_id INTEGER NOT NULL REFERENCES professions (id) ON DELETE RESTRICT,
    department_id INTEGER NOT NULL REFERENCES departments (id) ON DELETE RESTRICT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Справочник типов ТМЦ
CREATE TABLE IF NOT EXISTS tmc_types (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(20) NOT NULL UNIQUE CHECK (name IN ('SIZ', 'TOOL', 'EQUIPMENT')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Справочник ТМЦ (общая информация)
CREATE TABLE IF NOT EXISTS tmc_items (
                                         id SERIAL PRIMARY KEY,
                                         code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    type_id INTEGER NOT NULL REFERENCES tmc_types (id) ON DELETE RESTRICT,
    unit VARCHAR(20) NOT NULL,
    service_life_months INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Атрибуты СИЗ
CREATE TABLE IF NOT EXISTS siz_attributes (
                                              tmc_id INTEGER PRIMARY KEY REFERENCES tmc_items (id) ON DELETE CASCADE,
    size VARCHAR(20),
    wear_period_months INTEGER,
    protection_class VARCHAR(50)
    );

-- Атрибуты инструмента
CREATE TABLE IF NOT EXISTS tool_attributes (
                                               tmc_id INTEGER PRIMARY KEY REFERENCES tmc_items (id) ON DELETE CASCADE,
    material VARCHAR(100),
    gost_number VARCHAR(50),
    measurement_range VARCHAR(50)
    );

-- Атрибуты оснастки
CREATE TABLE IF NOT EXISTS equipment_attributes (
                                                    tmc_id INTEGER PRIMARY KEY REFERENCES tmc_items (id) ON DELETE CASCADE,
    drawing_number VARCHAR(50),
    max_cycles INTEGER,
    machine_model VARCHAR(100)
    );

-- Нормы выдачи ТМЦ
CREATE TABLE IF NOT EXISTS norms (
                                     id SERIAL PRIMARY KEY,
                                     tmc_id INTEGER NOT NULL REFERENCES tmc_items (id) ON DELETE CASCADE,
    profession_id INTEGER NOT NULL REFERENCES professions (id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    period_months INTEGER NOT NULL CHECK (period_months > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (tmc_id, profession_id)
    );

-- Кадровый план
CREATE TABLE IF NOT EXISTS staffing_plans (
                                              id SERIAL PRIMARY KEY,
                                              employee_id INTEGER NOT NULL REFERENCES employees (id) ON DELETE CASCADE,
    action_type VARCHAR(20) NOT NULL CHECK (action_type IN ('HIRE', 'TERMINATE', 'TRANSFER')),
    effective_date DATE NOT NULL,
    new_profession_id INTEGER REFERENCES professions (id) ON DELETE RESTRICT,
    new_department_id INTEGER REFERENCES departments (id) ON DELETE RESTRICT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Результаты расчёта
CREATE TABLE IF NOT EXISTS calculation_results (
                                                   id SERIAL PRIMARY KEY,
                                                   tmc_id INTEGER NOT NULL REFERENCES tmc_items (id) ON DELETE CASCADE,
    required_quantity INTEGER NOT NULL CHECK (required_quantity > 0),
    period_month INTEGER NOT NULL CHECK (period_month >= 1 AND period_month <= 12),
    period_year INTEGER NOT NULL CHECK (period_year >= 2020),
    calculation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Справочник ролей пользователей
CREATE TABLE IF NOT EXISTS user_roles (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(50) NOT NULL UNIQUE
    );

-- Пользователи системы
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    employee_id INTEGER UNIQUE REFERENCES employees (id) ON DELETE CASCADE,
    role_id INTEGER NOT NULL REFERENCES user_roles (id) ON DELETE RESTRICT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Индексы для ускорения запросов
CREATE INDEX IF NOT EXISTS idx_employees_profession ON employees (profession_id);
CREATE INDEX IF NOT EXISTS idx_employees_department ON employees (department_id);
CREATE INDEX IF NOT EXISTS idx_tmc_items_type ON tmc_items (type_id);
CREATE INDEX IF NOT EXISTS idx_norms_tmc ON norms (tmc_id);
CREATE INDEX IF NOT EXISTS idx_norms_profession ON norms (profession_id);
CREATE INDEX IF NOT EXISTS idx_staffing_plans_employee ON staffing_plans (employee_id);
CREATE INDEX IF NOT EXISTS idx_staffing_plans_date ON staffing_plans (effective_date);
CREATE INDEX IF NOT EXISTS idx_calculation_results_tmc ON calculation_results (tmc_id);
CREATE INDEX IF NOT EXISTS idx_calculation_results_period ON calculation_results (period_year, period_month);
CREATE INDEX IF NOT EXISTS idx_users_employee ON users (employee_id);
CREATE INDEX IF NOT EXISTS idx_users_role ON users (role_id);