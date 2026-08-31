ALTER TABLE staffing_plans
    ADD COLUMN old_profession_id INTEGER REFERENCES professions(id) ON DELETE SET NULL,
    ADD COLUMN old_department_id INTEGER REFERENCES departments(id) ON DELETE SET NULL;