INSERT INTO user_roles (id, name) VALUES
    (1, 'ROLE_OT'),
    (2, 'ROLE_TECHNOLOG'),
    (3, 'ROLE_STOREKEEPER'),
    (4, 'ROLE_LABOR'),
    (5, 'ROLE_MTS'),
    (6, 'ROLE_ADMIN')
ON CONFLICT (id) DO NOTHING;

SELECT setval('user_roles_id_seq', (SELECT MAX(id) FROM user_roles));

INSERT INTO departments (id, name) VALUES
    (1, 'Цех №3'),
    (2, 'Цех №5'),
    (3, 'Инструментально-штамповое управление'),
    (4, 'Управление главного технолога'),
    (5, 'Управление материально-технического снабжения'),
    (6, 'Отдел охраны труда и промышленной безопасности')
ON CONFLICT (id) DO NOTHING;

SELECT setval('departments_id_seq', (SELECT MAX(id) FROM departments));

INSERT INTO professions (id, name) VALUES
    (1, 'Токарь 5 разряда'),
    (2, 'Слесарь 4 разряда'),
    (3, 'Сварщик 6 разряда'),
    (4, 'Фрезеровщик 5 разряда'),
    (5, 'Инженер-технолог'),
    (6, 'Инженер по охране труда'),
    (7, 'Кладовщик ИРК'),
    (8, 'Инженер по МТС'),
    (9, 'Инженер по организации и нормированию труда')
ON CONFLICT (id) DO NOTHING;

SELECT setval('professions_id_seq', (SELECT MAX(id) FROM professions));

INSERT INTO employees (id, full_name, hire_date, profession_id, department_id) VALUES
    (1, 'Токарев Иван Петрович', '2024-01-15', 1, 1),   -- Токарь 5 разр., Цех №3
    (2, 'Слесарев Пётр Николаевич', '2024-03-20', 2, 2), -- Слесарь 4 разр., Цех №5
    (3, 'Сварщиков Дмитрий Сергеевич', '2024-06-10', 3, 2), -- Сварщик, Цех №5
    (4, 'Фрезеров Андрей Владимирович', '2025-01-10', 4, 1), -- Фрезеровщик, Цех №3
    (5, 'Козлов Никита Алексеевич', '2025-02-01', 5, 4),    -- Инженер-технолог (ROLE_TECHNOLOG)
    (6, 'Морозова Анастасия Сергеевна', '2025-03-01', 6, 6), -- Инженер по ОТ (ROLE_OT)
    (7, 'Волков Денис Андреевич', '2025-04-01', 7, 3),       -- Кладовщик ИРК (ROLE_STOREKEEPER)
    (8, 'Соколова Екатерина Михайловна', '2025-05-01', 8, 5), -- Инженер по МТС (ROLE_MTS)
    (9, 'Макаров Олег Викторович', '2025-06-01', 9, 4)   -- Инженер по ОиНТ (ROLE_LABOR)
ON CONFLICT (id) DO NOTHING;

SELECT setval('employees_id_seq', (SELECT MAX(id) FROM employees));

INSERT INTO users (id, username, password_hash, employee_id, role_id, is_active) VALUES
    (1, 'admin', '$2a$12$bGpFPzZ.KRfakJ4KTytyseBSjUyTiGXtFLPo2Jax6RFkfSjIRsQSS', NULL, 6, true),
    (2, 'kozlov_n', '$2a$12$3KAYy6lVG2WWhtlP6JGuq.J2p2B5uaXDXa8vsyxF1XyiIo8h426QC', 5, 2, true),  -- ROLE_TECHNOLOG
    (3, 'morozova_a', '$2a$12$wzAFPT67zXQ9wXaMC4hP3.sQX.oVRL39X4r9lJ3iw5mdaRNCD1pzy', 6, 1, true), -- ROLE_OT
    (4, 'volkov_d', '$2a$12$hNLKXdsHGDLT4UI6AR0sIee6KkJpQQMrrGrKNw7dzc04qMS2yljym', 7, 3, true),  -- ROLE_STOREKEEPER
    (5, 'sokolova_e', '$2a$12$ryZLQJhJziX4Vs9/8jlagegy4gFskZyH1fIO.FJtl.5DBbYh4D85K', 8, 5, true), -- ROLE_MTS
    (6, 'makarov_o', '$2a$12$hX1DHrUyyfiDD3jlf0.Q6OZyc/G/qTEd7pTJPzaWbIZsoUBfeacEy', 9, 4, true) -- ROLE_LABOR
ON CONFLICT (id) DO NOTHING;

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO tmc_types (id, name) VALUES
    (1, 'SIZ'),
    (2, 'TOOL'),
    (3, 'EQUIPMENT')
ON CONFLICT (id) DO NOTHING;

SELECT setval('tmc_types_id_seq', (SELECT MAX(id) FROM tmc_types));

INSERT INTO tmc_items (id, code, name, type_id, unit, service_life_months) VALUES
    (1, 'SIZ-001', 'Костюм мужской х/б', 1, 'компл', 24),
    (2, 'SIZ-002', 'Ботинки кожаные с защитным носком', 1, 'пара', 12),
    (3, 'SIZ-003', 'Перчатки трикотажные', 1, 'пара', 1),
    (4, 'SIZ-004', 'Каска защитная', 1, 'шт', 24),
    (5, 'SIZ-005', 'Очки защитные', 1, 'шт', 99),
    (6, 'TOOL-001', 'Штангенциркуль ШЦ-1', 2, 'шт', 24),
    (7, 'TOOL-002', 'Набор ключей рожковых 6-19', 2, 'шт', 36),
    (8, 'TOOL-003', 'Микрометр МК-25', 2, 'шт', 36),
    (9, 'TOOL-004', 'Отвёртка крестовая', 2, 'шт', 12),
    (10, 'EQ-001', 'Штамп гибочный №5', 3, 'шт', 60),
    (11, 'EQ-002', 'Кондуктор сверлильный', 3, 'шт', 48)
ON CONFLICT (id) DO NOTHING;

SELECT setval('tmc_items_id_seq', (SELECT MAX(id) FROM tmc_items));

INSERT INTO siz_attributes (tmc_id, size, wear_period_months, protection_class) VALUES
    (1, '100', 24, NULL),
    (2, '270', 12, 'МУН200'),
    (3, NULL, 1, NULL),
    (4, NULL, 24, NULL),
    (5, NULL, 99, 'ЗП')
ON CONFLICT (tmc_id) DO NOTHING;

INSERT INTO tool_attributes (tmc_id, material, gost_number, measurement_range) VALUES
    (6, 'Сталь 40Х', 'ГОСТ 166-89', '0-150'),
    (7, 'Сталь 45', 'ГОСТ 2839-80', NULL),
    (8, 'Сталь 40Х', 'ГОСТ 6507-90', '0-25'),
    (9, 'Сталь 40Х', 'ГОСТ 17199-88', NULL)
ON CONFLICT (tmc_id) DO NOTHING;

INSERT INTO equipment_attributes (tmc_id, drawing_number, max_cycles, machine_model) VALUES
    (10, '1234-5678', 50000, 'КД-2320'),
    (11, '8765-4321', 30000, '2С150')
ON CONFLICT (tmc_id) DO NOTHING;

INSERT INTO norms (tmc_id, profession_id, quantity, period_months) VALUES
    (6, 1, 1, 24),   -- Штангенциркуль: 1 шт на 24 мес
    (7, 1, 1, 36),   -- Набор ключей: 1 шт на 36 мес
    (3, 1, 6, 1),    -- Перчатки: 6 пар в месяц
    (4, 1, 1, 24)    -- Каска: 1 шт на 24 мес
ON CONFLICT DO NOTHING;

INSERT INTO norms (tmc_id, profession_id, quantity, period_months) VALUES
    (6, 2, 1, 24),   -- Штангенциркуль: 1 шт на 24 мес
    (7, 2, 1, 36),   -- Набор ключей: 1 шт на 36 мес
    (1, 2, 2, 24),   -- Костюм: 2 компл на 24 мес
    (2, 2, 1, 12),   -- Ботинки: 1 пара на 12 мес
    (3, 2, 6, 1)     -- Перчатки: 6 пар в месяц
ON CONFLICT DO NOTHING;

INSERT INTO norms (tmc_id, profession_id, quantity, period_months) VALUES
    (1, 3, 2, 24),   -- Костюм: 2 компл на 24 мес
    (2, 3, 1, 12),   -- Ботинки: 1 пара на 12 мес
    (3, 3, 12, 1),   -- Перчатки: 12 пар в месяц
    (4, 3, 1, 24)    -- Каска: 1 шт на 24 мес
ON CONFLICT DO NOTHING;

INSERT INTO norms (tmc_id, profession_id, quantity, period_months) VALUES
    (6, 4, 1, 24),   -- Штангенциркуль: 1 шт на 24 мес
    (3, 4, 6, 1),    -- Перчатки: 6 пар в месяц
    (4, 4, 1, 24)    -- Каска: 1 шт на 24 мес
ON CONFLICT DO NOTHING;

INSERT INTO staffing_plans (employee_id, action_type, effective_date, new_profession_id, new_department_id) VALUES
    (1, 'HIRE', '2026-06-01', 1, 1),   -- Иванов принят 01.06.2026
    (2, 'HIRE', '2026-06-15', 2, 2),   -- Петров принят 15.06.2026
    (3, 'HIRE', '2026-06-20', 3, 2),   -- Сидоров принят 20.06.2026
    (4, 'TRANSFER', '2026-07-01', 4, 2), -- Козлов переведён в Цех №5
    (5, 'TERMINATE', '2026-07-15', NULL, NULL) -- Новиков уволен
ON CONFLICT DO NOTHING;

SELECT setval('norms_id_seq', (SELECT MAX(id) FROM norms));
SELECT setval('staffing_plans_id_seq', (SELECT MAX(id) FROM staffing_plans));