INSERT INTO units (code, name) VALUES
    ('шт', 'Штука'),
    ('пара', 'Пара'),
    ('компл', 'Комплект'),
    ('набор', 'Набор'),
    ('м', 'Метр'),
    ('кг', 'Килограмм'),
    ('л', 'Литр'),
    ('уп', 'Упаковка'),
    ('рул', 'Рулон')
ON CONFLICT (code) DO NOTHING;

INSERT INTO departments (id, name) VALUES
    (1, 'Рамно-прессовый цех'),
    (2, 'Механосборочный цех №3'),
    (3, 'Механосборочный цех №4'),
    (4, 'Механосборочный цех №6'),
    (5, 'Механосборочный цех №9'),
    (6, 'Цех сборки и испытания автомобилей'),
    (7, 'Термический цех'),
    (8, 'Цех гибких производственных систем'),
    (9, 'Инструментально-штамповое управление'),
    (10, 'Управление главного технолога'),
    (11, 'Управление материально-технического снабжения'),
    (12, 'Отдел охраны труда и промышленной безопасности')
ON CONFLICT (id) DO NOTHING;

SELECT setval('departments_id_seq', (SELECT MAX(id) FROM departments));

INSERT INTO professions (id, name) VALUES
    (1, 'Кузнец-штамповщик'),
    (2, 'Прессовщик'),
    (3, 'Вальцовщик'),
    (4, 'Токарь'),
    (5, 'Токарь-карусельщик'),
    (6, 'Фрезеровщик'),
    (7, 'Сверловщик'),
    (8, 'Шлифовщик'),
    (9, 'Зуборезчик'),
    (10, 'Протяжчик'),
    (11, 'Слесарь-инструментальщик'),
    (12, 'Слесарь механосборочных работ'),
    (13, 'Наладчик станков с ЧПУ'),
    (14, 'Оператор станков с ЧПУ'),
    (15, 'Станочник широкого профиля'),
    (16, 'Слесарь механосборочных работ (сборка)'),
    (17, 'Электросварщик ручной сварки'),
    (18, 'Слесарь по ремонту автомобилей'),
    (19, 'Водитель-испытатель'),
    (20, 'Маляр'),
    (21, 'Термист'),
    (22, 'Травильщик'),
    (23, 'Гальваник'),
    (24, 'Оператор станков с ЧПУ (ГПС)'),
    (25, 'Наладчик станков с ЧПУ (ГПС)'),
    (26, 'Слесарь-инструментальщик (ИШУ)'),
    (27, 'Штамповщик'),
    (28, 'Инженер-технолог'),
    (29, 'Инженер по охране труда'),
    (30, 'Кладовщик ИРК'),
    (31, 'Инженер по МТС'),
    (32, 'Инженер по организации и нормированию труда')
ON CONFLICT (id) DO NOTHING;

SELECT setval('professions_id_seq', (SELECT MAX(id) FROM professions));

INSERT INTO user_roles (id, name) VALUES
    (1, 'ROLE_OT'),
    (2, 'ROLE_TECHNOLOG'),
    (3, 'ROLE_STOREKEEPER'),
    (4, 'ROLE_LABOR'),
    (5, 'ROLE_MTS'),
    (6, 'ROLE_ADMIN')
ON CONFLICT (id) DO NOTHING;

SELECT setval('user_roles_id_seq', (SELECT MAX(id) FROM user_roles));

INSERT INTO tmc_types (id, name) VALUES
    (1, 'SIZ'),
    (2, 'TOOL'),
    (3, 'EQUIPMENT')
ON CONFLICT (id) DO NOTHING;

SELECT setval('tmc_types_id_seq', (SELECT MAX(id) FROM tmc_types));

INSERT INTO tmc_items (code, name, type_id, unit_id, service_life_months) VALUES
    ('С00000001', 'Костюм мужской х/б', 1, (SELECT id FROM units WHERE code = 'компл'), 24),
    ('С00000002', 'Головной убор из х/б ткани', 1, (SELECT id FROM units WHERE code = 'шт'), 24),
    ('С00000003', 'Ботинки кожаные с защитным носком', 1, (SELECT id FROM units WHERE code = 'пара'), 12),
    ('С00000004', 'Рукавицы комбинированные', 1, (SELECT id FROM units WHERE code = 'пара'), 1),
    ('С00000005', 'Перчатки трикотажные', 1, (SELECT id FROM units WHERE code = 'пара'), 1),
    ('С00000006', 'Каска защитная', 1, (SELECT id FROM units WHERE code = 'шт'), 24),
    ('С00000007', 'Очки защитные', 1, (SELECT id FROM units WHERE code = 'шт'), 99),
    ('С00000008', 'Щиток защитный лицевой НБТ', 1, (SELECT id FROM units WHERE code = 'шт'), 99),
    ('С00000009', 'Наушники противошумные', 1, (SELECT id FROM units WHERE code = 'шт'), 99),
    ('С00000010', 'Рукавицы для защиты от вибрации', 1, (SELECT id FROM units WHERE code = 'пара'), 99),
    ('С00000011', 'Перчатки антивибрационные', 1, (SELECT id FROM units WHERE code = 'шт'), 99),
    ('С00000012', 'Очки защитные модели типа ARCTIC NORTH', 1, (SELECT id FROM units WHERE code = 'шт'), 99),
    ('С00000013', 'Очки защитные (для цехов)', 1, (SELECT id FROM units WHERE code = 'шт'), 99),
    ('С00000014', 'Перчатки утеплённые', 1, (SELECT id FROM units WHERE code = 'пара'), 12),
    ('С00000015', 'Рукавицы утеплённые', 1, (SELECT id FROM units WHERE code = 'пара'), 12),
    ('С00000016', 'Ботинки кожаные на полиуретановой подошве', 1, (SELECT id FROM units WHERE code = 'пара'), 12),
    ('С00000017', 'Полуботинки кожаные', 1, (SELECT id FROM units WHERE code = 'пара'), 12),
    ('С00000018', 'Ботинки кожаные на МБС подошве', 1, (SELECT id FROM units WHERE code = 'пара'), 12),
    ('С00000019', 'Сапоги кирзовые', 1, (SELECT id FROM units WHERE code = 'пара'), 24),
    ('С00000020', 'Сапоги кирзовые утеплённые с защитным носком', 1, (SELECT id FROM units WHERE code = 'пара'), 24),
    ('С00000021', 'Костюм утеплённый из водонепроницаемой ткани', 1, (SELECT id FROM units WHERE code = 'компл'), 24),
    ('С00000022', 'Костюм вискозно-лавсановый с МВО отделкой', 1, (SELECT id FROM units WHERE code = 'компл'), 24),
    ('С00000023', 'Костюм сварщика', 1, (SELECT id FROM units WHERE code = 'компл'), 24),
    ('С00000024', 'Костюм сварщика зимний', 1, (SELECT id FROM units WHERE code = 'компл'), 24),
    ('С00000025', 'Костюм непромокаемый', 1, (SELECT id FROM units WHERE code = 'компл'), 24),
    ('С00000026', 'Плащ непромокаемый с капюшоном', 1, (SELECT id FROM units WHERE code = 'шт'), 24),
    ('С00000027', 'Костюм прорезиненный', 1, (SELECT id FROM units WHERE code = 'компл'), 24),
    ('С00000028', 'Нарукавники брезентовые', 1, (SELECT id FROM units WHERE code = 'пара'), 6),
    ('С00000029', 'Рукавицы брезентовые', 1, (SELECT id FROM units WHERE code = 'пара'), 1),
    ('С00000030', 'Перчатки резиновые', 1, (SELECT id FROM units WHERE code = 'пара'), 1),

    ('И00000001', 'Штангенциркуль ШЦ-1 0-150', 2, (SELECT id FROM units WHERE code = 'шт'), 24),
    ('И00000002', 'Микрометр МК-25 0-25', 2, (SELECT id FROM units WHERE code = 'шт'), 36),
    ('И00000003', 'Набор ключей рожковых 6-19', 2, (SELECT id FROM units WHERE code = 'набор'), 36),
    ('И00000004', 'Отвёртка крестовая', 2, (SELECT id FROM units WHERE code = 'шт'), 12),
    ('И00000005', 'Набор свёрл по металлу', 2, (SELECT id FROM units WHERE code = 'набор'), 12),
    ('И00000006', 'Метчик М6х1', 2, (SELECT id FROM units WHERE code = 'шт'), 6),
    ('И00000007', 'Плашка М6', 2, (SELECT id FROM units WHERE code = 'шт'), 6),
    ('И00000008', 'Резец токарный проходной', 2, (SELECT id FROM units WHERE code = 'шт'), 3),
    ('И00000009', 'Набор фрез концевых', 2, (SELECT id FROM units WHERE code = 'набор'), 12),
    ('И00000010', 'Сверло спиральное 10мм', 2, (SELECT id FROM units WHERE code = 'шт'), 3),
    ('И00000011', 'Развертка 10мм', 2, (SELECT id FROM units WHERE code = 'шт'), 6),
    ('И00000012', 'Плашка М8', 2, (SELECT id FROM units WHERE code = 'шт'), 6),
    ('И00000013', 'Метчик М8х1,25', 2, (SELECT id FROM units WHERE code = 'шт'), 6),

    ('О00000001', 'Штамп гибочный №5', 3, (SELECT id FROM units WHERE code = 'шт'), 60),
    ('О00000002', 'Кондуктор сверлильный', 3, (SELECT id FROM units WHERE code = 'шт'), 48),
    ('О00000003', 'Кронштейн', 3, (SELECT id FROM units WHERE code = 'шт'), 36),
    ('О00000004', 'Опора', 3, (SELECT id FROM units WHERE code = 'шт'), 36),
    ('О00000005', 'Пластина', 3, (SELECT id FROM units WHERE code = 'шт'), 24),
    ('О00000006', 'Клеть', 3, (SELECT id FROM units WHERE code = 'шт'), 60),
    ('О00000007', 'Вилка', 3, (SELECT id FROM units WHERE code = 'шт'), 36),
    ('О00000008', 'Корпус водила', 3, (SELECT id FROM units WHERE code = 'шт'), 60),
    ('О00000009', 'Картер моста', 3, (SELECT id FROM units WHERE code = 'шт'), 60),
    ('О00000010', 'Наконечник', 3, (SELECT id FROM units WHERE code = 'шт'), 24)
ON CONFLICT (code) DO NOTHING;

SELECT setval('tmc_items_id_seq', (SELECT MAX(id) FROM tmc_items));

INSERT INTO siz_attributes (tmc_id, size, wear_period_months, protection_class) VALUES
    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), '100', 24, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), '270', 12, 'МУН200'),
    ((SELECT id FROM tmc_items WHERE code = 'С00000016'), '270', 12, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'С00000017'), '270', 12, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'С00000018'), '270', 12, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'С00000020'), '280', 24, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'С00000019'), '280', 24, NULL)
ON CONFLICT (tmc_id) DO NOTHING;

INSERT INTO tool_attributes (tmc_id, material, gost_number, measurement_range) VALUES
    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 'Сталь 40Х', 'ГОСТ 166-89', '0-150'),
    ((SELECT id FROM tmc_items WHERE code = 'И00000002'), 'Сталь 40Х', 'ГОСТ 6507-90', '0-25'),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 'Сталь 45', 'ГОСТ 2839-80', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000004'), 'Сталь 40Х', 'ГОСТ 17199-88', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000005'), 'Р6М5', 'ГОСТ 10903-77', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000006'), 'Р6М5', 'ГОСТ 3266-81', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000007'), 'Р6М5', 'ГОСТ 9740-71', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000008'), 'Р6М5', 'ГОСТ 18877-73', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000009'), 'Р6М5', 'ГОСТ 17025-71', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000010'), 'Р6М5', 'ГОСТ 10903-77', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000011'), 'Р6М5', 'ГОСТ 7722-77', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000012'), 'Р6М5', 'ГОСТ 9740-71', NULL),
    ((SELECT id FROM tmc_items WHERE code = 'И00000013'), 'Р6М5', 'ГОСТ 3266-81', NULL)
ON CONFLICT (tmc_id) DO NOTHING;

INSERT INTO equipment_attributes (tmc_id, drawing_number, max_cycles, machine_model) VALUES
    ((SELECT id FROM tmc_items WHERE code = 'О00000001'), '1234-5678', 50000, 'КД-2320'),
    ((SELECT id FROM tmc_items WHERE code = 'О00000002'), '8765-4321', 30000, '2С150'),
    ((SELECT id FROM tmc_items WHERE code = 'О00000003'), '652511-8603181', 25000, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'О00000004'), '652511-8606270', 30000, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'О00000005'), '6515-8606330', 20000, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'О00000006'), '692375-9200010', 50000, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'О00000007'), '64221-1703497', 15000, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'О00000008'), '8007-2305030', 40000, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'О00000009'), '8007-2301012-10', 35000, NULL),
    ((SELECT id FROM tmc_items WHERE code = 'О00000010'), '7930-3414077-10', 20000, NULL)
ON CONFLICT (tmc_id) DO NOTHING;

INSERT INTO norms (tmc_id, profession_id, quantity, period_months) VALUES
    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 4, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 4, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 4, 6, 1),
    ((SELECT id FROM tmc_items WHERE code = 'С00000006'), 4, 1, 24),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 5, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 5, 6, 1),
    ((SELECT id FROM tmc_items WHERE code = 'С00000006'), 5, 1, 24),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 6, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 6, 6, 1),
    ((SELECT id FROM tmc_items WHERE code = 'С00000006'), 6, 1, 24),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 7, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 7, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 8, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 8, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 9, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 9, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 10, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 10, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 11, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 11, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 11, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 11, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 12, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 12, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 12, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 12, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 12, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 13, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 13, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 13, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 14, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 14, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 15, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 15, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 15, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 16, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 16, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 16, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 16, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 16, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000023'), 17, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000024'), 17, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 17, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 17, 12, 1),
    ((SELECT id FROM tmc_items WHERE code = 'С00000008'), 17, 1, 99),

    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 18, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 18, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 18, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 18, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 19, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 19, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 19, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 20, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 20, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 20, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 21, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000020'), 21, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000004'), 21, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000022'), 22, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000020'), 22, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000030'), 22, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000022'), 23, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000030'), 23, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 24, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 24, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 25, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 25, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 25, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'И00000001'), 26, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'И00000003'), 26, 1, 36),
    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 26, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 26, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 26, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 27, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 27, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 27, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 1, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 1, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 1, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 2, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000003'), 2, 1, 12),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 2, 6, 1),

    ((SELECT id FROM tmc_items WHERE code = 'С00000001'), 3, 2, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000020'), 3, 1, 24),
    ((SELECT id FROM tmc_items WHERE code = 'С00000005'), 3, 6, 1)
ON CONFLICT DO NOTHING;

SELECT setval('norms_id_seq', (SELECT MAX(id) FROM norms));

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Кузнецов Андрей Викторович', '2013-01-10', 28, 10),
    ('Соловьёв Дмитрий Александрович', '2016-02-15', 28, 10),
    ('Морозова Анастасия Сергеевна', '2014-03-01', 29, 12),
    ('Ильина Елена Владимировна', '2020-04-01', 29, 12),
    ('Волков Денис Андреевич', '2025-05-01', 30, 9),
    ('Никитин Павел Игоревич', '2018-06-01', 30, 9),
    ('Соколова Екатерина Михайловна', '2018-07-01', 31, 11),
    ('Григорьев Сергей Николаевич', '2026-08-01', 31, 11),
    ('Макаров Олег Викторович', '2025-09-01', 32, 10),
    ('Фёдорова Татьяна Петровна', '2024-10-01', 32, 10)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Анисимов Игорь Владимирович', '2014-01-15', 1, 1),
    ('Белов Сергей Александрович', '2004-02-10', 2, 1),
    ('Громов Пётр Николаевич', '2011-03-05', 3, 1),
    ('Данилов Алексей Иванович', '2018-04-01', 1, 1),
    ('Егоров Василий Петрович', '2026-05-15', 2, 1),
    ('Жуков Роман Сергеевич', '2018-06-10', 3, 1),
    ('Зайцев Михаил Андреевич', '2007-07-01', 1, 1),
    ('Исаев Николай Дмитриевич', '2012-08-15', 2, 1),
    ('Казаков Олег Викторович', '2013-09-10', 3, 1),
    ('Ларин Денис Александрович', '2023-10-01', 1, 1),
    ('Медведев Евгений Сергеевич', '2024-11-15', 2, 1),
    ('Никонов Павел Андреевич', '2024-12-10', 3, 1)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Орлов Александр Валерьевич', '2004-01-20', 4, 2),
    ('Панкратов Илья Николаевич', '2018-02-25', 5, 2),
    ('Романов Григорий Петрович', '2019-03-15', 6, 2),
    ('Семёнов Дмитрий Иванович', '2006-04-10', 7, 2),
    ('Тимофеев Сергей Александрович', '2006-05-05', 8, 2),
    ('Устинов Анатолий Владимирович', '2012-06-20', 9, 2),
    ('Филиппов Василий Сергеевич', '2024-07-15', 10, 2),
    ('Харитонов Роман Алексеевич', '2024-08-10', 11, 2),
    ('Чернов Андрей Николаевич', '2023-09-05', 12, 2),
    ('Широков Михаил Петрович', '2025-10-20', 13, 2),
    ('Щербаков Денис Валерьевич', '2023-11-15', 14, 2),
    ('Яковлев Евгений Андреевич', '2023-12-10', 15, 2)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Баранов Александр Иванович', '2026-01-25', 4, 3),
    ('Власов Пётр Сергеевич', '2009-02-20', 6, 3),
    ('Гусев Дмитрий Николаевич', '2018-03-10', 7, 3),
    ('Демидов Андрей Валерьевич', '2018-04-15', 8, 3),
    ('Ермаков Илья Александрович', '2013-05-20', 11, 3),
    ('Зимин Роман Петрович', '2014-06-15', 12, 3),
    ('Королёв Сергей Иванович', '2026-07-10', 13, 3),
    ('Лобов Анатолий Дмитриевич', '2026-08-05', 14, 3),
    ('Мишин Василий Николаевич', '2015-09-20', 15, 3),
    ('Носов Александр Андреевич', '2016-10-15', 4, 3),
    ('Овчинников Алексей Сергеевич', '2017-11-10', 6, 3),
    ('Попов Денис Валерьевич', '2020-12-05', 7, 3)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Савинов Иван Григорьевич', '2024-01-30', 4, 4),
    ('Третьяков Пётр Алексеевич', '2023-02-15', 8, 4),
    ('Фокин Дмитрий Сергеевич', '2013-03-20', 9, 4),
    ('Цветков Андрей Валерьевич', '2015-04-25', 11, 4),
    ('Швецов Александр Николаевич', '2016-05-15', 12, 4),
    ('Щукин Роман Иванович', '2020-06-20', 13, 4),
    ('Юрьев Михаил Петрович', '2021-07-25', 14, 4),
    ('Астахов Сергей Александрович', '2021-08-15', 15, 4),
    ('Букин Денис Андреевич', '2021-09-20', 4, 4),
    ('Варламов Илья Сергеевич', '2018-10-25', 8, 4),
    ('Горбунов Анатолий Валерьевич', '2009-11-15', 9, 4),
    ('Дорохов Василий Николаевич', '2010-12-20', 11, 4)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Евсеев Александр Петрович', '2023-01-10', 4, 5),
    ('Жилин Дмитрий Иванович', '2021-02-25', 6, 5),
    ('Зверев Сергей Александрович', '2022-03-15', 7, 5),
    ('Киселёв Андрей Владимирович', '2022-04-10', 12, 5),
    ('Козырев Пётр Николаевич', '2025-05-05', 13, 5),
    ('Лукьянов Михаил Сергеевич', '2026-06-20', 14, 5),
    ('Максимов Иван Валерьевич', '2021-07-15', 15, 5),
    ('Назаров Анатолий Александрович', '2020-08-10', 4, 5),
    ('Орехов Денис Петрович', '2020-09-05', 6, 5),
    ('Поляков Андрей Иванович', '2021-10-20', 7, 5),
    ('Рогов Роман Сергеевич', '2024-11-15', 12, 5),
    ('Степанов Александр Валерьевич', '2024-12-10', 13, 5)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Тарасов Дмитрий Николаевич', '2014-01-15', 16, 6),
    ('Уваров Сергей Петрович', '2018-02-20', 17, 6),
    ('Фролов Илья Александрович', '2019-03-10', 18, 6),
    ('Хохлов Андрей Валерьевич', '2024-04-15', 19, 6),
    ('Царёв Василий Иванович', '2023-05-20', 20, 6),
    ('Чаплыгин Роман Сергеевич', '2022-06-15', 16, 6),
    ('Шестаков Михаил Петрович', '2021-07-10', 17, 6),
    ('Шубин Анатолий Дмитриевич', '2009-08-05', 18, 6),
    ('Эпов Александр Николаевич', '2010-09-20', 19, 6),
    ('Юшин Денис Андреевич', '2011-10-15', 20, 6),
    ('Блинов Илья Сергеевич', '2024-11-10', 16, 6),
    ('Галкин Пётр Валерьевич', '2025-12-05', 17, 6)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Дубровин Александр Иванович', '2023-01-20', 21, 7),
    ('Елисеев Дмитрий Петрович', '2013-02-15', 22, 7),
    ('Жданов Сергей Александрович', '2014-03-20', 23, 7),
    ('Кузьмин Андрей Валерьевич', '2025-04-25', 21, 7),
    ('Мальцев Роман Николаевич', '2026-05-15', 22, 7),
    ('Наумов Илья Иванович', '2018-06-20', 23, 7),
    ('Осипов Василий Сергеевич', '2019-07-25', 21, 7),
    ('Песков Михаил Петрович', '2020-08-15', 22, 7),
    ('Разумов Анатолий Дмитриевич', '2013-09-20', 23, 7),
    ('Сафронов Александр Валерьевич', '2017-10-25', 21, 7)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Тихонов Денис Сергеевич', '2014-01-10', 24, 8),
    ('Ульянов Андрей Петрович', '2017-02-25', 25, 8),
    ('Филимонов Илья Николаевич', '2021-03-15', 24, 8),
    ('Хабаров Роман Валерьевич', '2024-04-10', 25, 8),
    ('Чесноков Александр Иванович', '2026-05-05', 24, 8),
    ('Шаламов Дмитрий Сергеевич', '2019-06-20', 25, 8),
    ('Эрлих Анатолий Александрович', '2019-07-15', 24, 8),
    ('Юдин Михаил Петрович', '2020-08-10', 25, 8),
    ('Ярцев Сергей Валерьевич', '2021-09-05', 24, 8),
    ('Антонов Денис Андреевич', '2023-10-20', 25, 8)
ON CONFLICT DO NOTHING;

INSERT INTO users (username, password_hash, employee_id, role_id, is_active) VALUES
    ('kuznetsov_a', '$2a$12$wGNtXnraPdEcrIvnhWLa0uomoe4Vbi/6ejti/Ey1gELfbR09xbJr2', (SELECT id FROM employees WHERE full_name = 'Кузнецов Андрей Викторович'), 2, true),
    ('solovyov_d', '$2a$12$wxgfO3uYAcCbQc0I7pBbhu/efOv8I6jjA4XBcNnY2cdlLq3VBv1DK', (SELECT id FROM employees WHERE full_name = 'Соловьёв Дмитрий Александрович'), 2, true),
    ('morozova_a', '$2a$12$RmAhFUkBm3c4KtI//IyGYOo3Yc4wfCv6lu4m21Ega.7RYgeqDgbMS', (SELECT id FROM employees WHERE full_name = 'Морозова Анастасия Сергеевна'), 1, true),
    ('ilina_e', '$2a$12$XnNbk0buY2/1POCkNrg0oe/TMigMSUVw7xF9teeYsNyy1Odf9Hh8G', (SELECT id FROM employees WHERE full_name = 'Ильина Елена Владимировна'), 1, true),
    ('volkov_d', '$2a$12$yA0b6TUvEBsBSNWUQrsN7euZ2xHpmnMeAPsTIUAKmv.UMUMVrpENW', (SELECT id FROM employees WHERE full_name = 'Волков Денис Андреевич'), 3, true),
    ('nikitin_p', '$2a$12$3ybr8kaIMMLhtt17Gzv6/u95OMsEmHqmg6bprwO/z0BDdbjXh1fK2', (SELECT id FROM employees WHERE full_name = 'Никитин Павел Игоревич'), 3, true),
    ('sokolova_e', '$2a$12$1vgz2ib97i.ACBD.1LORee8C.hPfJnirNfTL7J7Rx7d7uZ822it4O', (SELECT id FROM employees WHERE full_name = 'Соколова Екатерина Михайловна'), 5, true),
    ('grigoriev_s', '$2a$12$yhgiAcOk2FYuku6ptVbUA.1Bfp1tvm3f4LJr3nGDpzlH4oYSTbBQS', (SELECT id FROM employees WHERE full_name = 'Григорьев Сергей Николаевич'), 5, true),
    ('makarov_o', '$2a$12$nonsbeQVax1FkwNAuqgAzepy83N5/RPBaLw3/L3pGmRMGXFMUg4py', (SELECT id FROM employees WHERE full_name = 'Макаров Олег Викторович'), 4, true),
    ('fedorova_t', '$2a$12$k0WZCgLN3ipCLv2KUNgJsOaAuehVNaB2FT6ZJ2lqb2RB0Ekz8TAaa', (SELECT id FROM employees WHERE full_name = 'Фёдорова Татьяна Петровна'), 4, true),
    ('admin', '$2a$12$jF6qDndj.zh8D9nCG4.idOpwuIR29r7whmCs0UG8AinYcgxCumQ/e', NULL, 6, true)
ON CONFLICT DO NOTHING;

SELECT setval('employees_id_seq', (SELECT MAX(id) FROM employees));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));