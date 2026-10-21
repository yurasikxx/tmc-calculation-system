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
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 4,  2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 5,  2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 6,  2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 7,  2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 8,  2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 9,  2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 10, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 13, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 14, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 15, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 17, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 18, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 19, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 20, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 21, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 22, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 23, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 24, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 25, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 26, 2, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000001'), 27, 2, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000002'), 1,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 2,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 3,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 4,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 5,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 6,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 7,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 8,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 9,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 10, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 11, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 12, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 13, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 14, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 15, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 16, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 17, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 18, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 19, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 20, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 21, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 22, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 23, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 24, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 25, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 26, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000002'), 27, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000003'), 1,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 2,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 3,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 4,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 5,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 6,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 7,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 8,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 9,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 10, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 11, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 13, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 14, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 15, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 17, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 19, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 20, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 21, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 22, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 23, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 24, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 25, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 26, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000003'), 27, 1, 12),

    ((SELECT id FROM tmc_items WHERE code='С00000004'), 1,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 2,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 3,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 4,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 5,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 6,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 7,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 8,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 9,  6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 10, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 11, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 12, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 13, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 14, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 15, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 16, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 17, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 18, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 19, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 20, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 21, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 22, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 23, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 24, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 25, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 26, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000004'), 27, 6, 1),

    ((SELECT id FROM tmc_items WHERE code='С00000007'), 4,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 6,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 8,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 9,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 11, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 12, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 13, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 14, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 15, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 16, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 17, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 18, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 19, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 20, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 21, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 22, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 23, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 24, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 25, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 26, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000007'), 27, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000009'), 1,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 2,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 3,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 4,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 5,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 6,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 7,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 8,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 9,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 10, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 13, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 14, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 15, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 17, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 24, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000009'), 25, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000012'), 17, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000012'), 4,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000012'), 6,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000012'), 8,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000012'), 11, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000012'), 12, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000012'), 14, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000012'), 15, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000013'), 1,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 2,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 3,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 4,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 5,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 6,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 7,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 8,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 9,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 10, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 11, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 12, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 13, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 14, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 15, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 16, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 17, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 18, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 19, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 20, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 21, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 22, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 23, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 24, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 25, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 26, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000013'), 27, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000014'), 1,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 2,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 3,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 4,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 6,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 7,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 8,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 11, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 12, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 13, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 14, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 15, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 16, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 17, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 18, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 19, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 20, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 21, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 22, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 23, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 24, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 25, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 26, 2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000014'), 27, 2, 12),

    ((SELECT id FROM tmc_items WHERE code='С00000015'), 1,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000015'), 2,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000015'), 3,  2, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000015'), 17, 2, 12),

    ((SELECT id FROM tmc_items WHERE code='С00000016'), 13, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000016'), 14, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000016'), 15, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000016'), 24, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000016'), 25, 1, 12),

    ((SELECT id FROM tmc_items WHERE code='С00000017'), 4,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000017'), 6,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000017'), 7,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000017'), 8,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000017'), 11, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000017'), 12, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000017'), 16, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000017'), 26, 1, 12),

    ((SELECT id FROM tmc_items WHERE code='С00000018'), 17, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000018'), 18, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='С00000018'), 19, 1, 12),

    ((SELECT id FROM tmc_items WHERE code='С00000019'), 4,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000019'), 6,  1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000019'), 17, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000019'), 18, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000025'), 19, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000025'), 20, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000026'), 19, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000026'), 18, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000027'), 22, 1, 24),
    ((SELECT id FROM tmc_items WHERE code='С00000027'), 23, 1, 24),

    ((SELECT id FROM tmc_items WHERE code='С00000028'), 17, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='С00000028'), 1,  2, 6),

    ((SELECT id FROM tmc_items WHERE code='С00000029'), 17, 6, 1),
    ((SELECT id FROM tmc_items WHERE code='С00000029'), 21, 6, 1),

    ((SELECT id FROM tmc_items WHERE code='И00000002'), 4,  1, 36),
    ((SELECT id FROM tmc_items WHERE code='И00000002'), 6,  1, 36),
    ((SELECT id FROM tmc_items WHERE code='И00000002'), 8,  1, 36),
    ((SELECT id FROM tmc_items WHERE code='И00000002'), 9,  1, 36),
    ((SELECT id FROM tmc_items WHERE code='И00000002'), 26, 1, 36),

    ((SELECT id FROM tmc_items WHERE code='И00000004'), 4,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 6,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 7,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 11, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 12, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 13, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 14, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 15, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 16, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 18, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000004'), 26, 1, 12),

    ((SELECT id FROM tmc_items WHERE code='И00000005'), 4,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000005'), 6,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000005'), 7,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000005'), 11, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000005'), 12, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000005'), 13, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000005'), 14, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000005'), 15, 1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000005'), 16, 1, 12),

    ((SELECT id FROM tmc_items WHERE code='И00000006'), 4,  2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000006'), 6,  2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000006'), 7,  2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000006'), 11, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000006'), 12, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000006'), 13, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000006'), 14, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000006'), 15, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000006'), 16, 2, 6),

    ((SELECT id FROM tmc_items WHERE code='И00000007'), 4,  1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000007'), 6,  1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000007'), 7,  1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000007'), 11, 1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000007'), 12, 1, 6),

    ((SELECT id FROM tmc_items WHERE code='И00000008'), 4,  3, 3),
    ((SELECT id FROM tmc_items WHERE code='И00000008'), 5,  2, 3),

    ((SELECT id FROM tmc_items WHERE code='И00000009'), 6,  1, 12),
    ((SELECT id FROM tmc_items WHERE code='И00000009'), 7,  1, 12),

    ((SELECT id FROM tmc_items WHERE code='И00000010'), 4,  3, 3),
    ((SELECT id FROM tmc_items WHERE code='И00000010'), 6,  3, 3),
    ((SELECT id FROM tmc_items WHERE code='И00000010'), 7,  3, 3),
    ((SELECT id FROM tmc_items WHERE code='И00000010'), 11, 3, 3),
    ((SELECT id FROM tmc_items WHERE code='И00000010'), 12, 3, 3),

    ((SELECT id FROM tmc_items WHERE code='И00000011'), 4,  1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000011'), 6,  1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000011'), 7,  1, 6),

    ((SELECT id FROM tmc_items WHERE code='И00000012'), 4,  1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000012'), 6,  1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000012'), 7,  1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000012'), 11, 1, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000012'), 12, 1, 6),

    ((SELECT id FROM tmc_items WHERE code='И00000013'), 4,  2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000013'), 6,  2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000013'), 7,  2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000013'), 11, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000013'), 12, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000013'), 13, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000013'), 14, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000013'), 15, 2, 6),
    ((SELECT id FROM tmc_items WHERE code='И00000013'), 16, 2, 6)
ON CONFLICT (tmc_id, profession_id) DO NOTHING;

SELECT setval('norms_id_seq', (SELECT MAX(id) FROM norms));

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Кузнецов Андрей Викторович', '2023-01-10', 28, 10),
    ('Соловьёв Дмитрий Александрович', '2026-02-15', 28, 10),
    ('Морозова Анастасия Сергеевна', '2024-03-01', 29, 12),
    ('Ильина Елена Владимировна', '2020-04-01', 29, 12),
    ('Волков Денис Андреевич', '2025-05-01', 30, 9),
    ('Никитин Павел Игоревич', '2022-06-01', 30, 9),
    ('Соколова Екатерина Михайловна', '2023-07-01', 31, 11),
    ('Григорьев Сергей Николаевич', '2026-08-01', 31, 11),
    ('Макаров Олег Викторович', '2025-09-01', 32, 10),
    ('Фёдорова Татьяна Петровна', '2024-10-01', 32, 10)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Анисимов Игорь Владимирович', '2024-01-15', 1, 1),
    ('Белов Сергей Александрович', '2024-02-10', 2, 1),
    ('Громов Пётр Николаевич', '2021-03-05', 3, 1),
    ('Данилов Алексей Иванович', '2024-04-01', 1, 1),
    ('Егоров Василий Петрович', '2026-05-15', 2, 1),
    ('Жуков Роман Сергеевич', '2023-06-10', 3, 1),
    ('Зайцев Михаил Андреевич', '2024-07-01', 1, 1),
    ('Исаев Николай Дмитриевич', '2022-08-15', 2, 1),
    ('Казаков Олег Викторович', '2023-09-10', 3, 1),
    ('Ларин Денис Александрович', '2023-10-01', 1, 1),
    ('Медведев Евгений Сергеевич', '2024-11-15', 2, 1),
    ('Никонов Павел Андреевич', '2024-12-10', 3, 1)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Орлов Александр Валерьевич', '2024-01-20', 4, 2),
    ('Панкратов Илья Николаевич', '2022-02-25', 5, 2),
    ('Романов Григорий Петрович', '2023-03-15', 6, 2),
    ('Семёнов Дмитрий Иванович', '2026-04-10', 7, 2),
    ('Тимофеев Сергей Александрович', '2026-05-05', 8, 2),
    ('Устинов Анатолий Владимирович', '2022-06-20', 9, 2),
    ('Филиппов Василий Сергеевич', '2022-07-15', 10, 2),
    ('Харитонов Роман Алексеевич', '2024-08-10', 11, 2),
    ('Чернов Андрей Николаевич', '2023-09-05', 12, 2),
    ('Широков Михаил Петрович', '2025-10-20', 13, 2),
    ('Щербаков Денис Валерьевич', '2023-11-15', 14, 2),
    ('Яковлев Евгений Андреевич', '2023-12-10', 15, 2)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Баранов Александр Иванович', '2026-01-25', 4, 3),
    ('Власов Пётр Сергеевич', '2023-02-20', 6, 3),
    ('Гусев Дмитрий Николаевич', '2022-03-10', 7, 3),
    ('Демидов Андрей Валерьевич', '2024-04-15', 8, 3),
    ('Ермаков Илья Александрович', '2023-05-20', 11, 3),
    ('Зимин Роман Петрович', '2024-06-15', 12, 3),
    ('Королёв Сергей Иванович', '2026-07-10', 13, 3),
    ('Лобов Анатолий Дмитриевич', '2026-08-05', 14, 3),
    ('Мишин Василий Николаевич', '2025-09-20', 15, 3),
    ('Носов Александр Андреевич', '2022-10-15', 4, 3),
    ('Овчинников Алексей Сергеевич', '2025-11-10', 6, 3),
    ('Попов Денис Валерьевич', '2020-12-05', 7, 3)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Савинов Иван Григорьевич', '2024-01-30', 4, 4),
    ('Третьяков Пётр Алексеевич', '2023-02-15', 8, 4),
    ('Фокин Дмитрий Сергеевич', '2023-03-20', 9, 4),
    ('Цветков Андрей Валерьевич', '2025-04-25', 11, 4),
    ('Швецов Александр Николаевич', '2024-05-15', 12, 4),
    ('Щукин Роман Иванович', '2020-06-20', 13, 4),
    ('Юрьев Михаил Петрович', '2021-07-25', 14, 4),
    ('Астахов Сергей Александрович', '2021-08-15', 15, 4),
    ('Букин Денис Андреевич', '2021-09-20', 4, 4),
    ('Варламов Илья Сергеевич', '2023-10-25', 8, 4),
    ('Горбунов Анатолий Валерьевич', '2022-11-15', 9, 4),
    ('Дорохов Василий Николаевич', '2020-12-20', 11, 4)
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
    ('Уваров Сергей Петрович', '2026-02-20', 17, 6),
    ('Фролов Илья Александрович', '2025-03-10', 18, 6),
    ('Хохлов Андрей Валерьевич', '2024-04-15', 19, 6),
    ('Царёв Василий Иванович', '2023-05-20', 20, 6),
    ('Чаплыгин Роман Сергеевич', '2022-06-15', 16, 6),
    ('Шестаков Михаил Петрович', '2021-07-10', 17, 6),
    ('Шубин Анатолий Дмитриевич', '2021-08-05', 18, 6),
    ('Эпов Александр Николаевич', '2020-09-20', 19, 6),
    ('Юшин Денис Андреевич', '2021-10-15', 20, 6),
    ('Блинов Илья Сергеевич', '2024-11-10', 16, 6),
    ('Галкин Пётр Валерьевич', '2025-12-05', 17, 6)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Дубровин Александр Иванович', '2023-01-20', 21, 7),
    ('Елисеев Дмитрий Петрович', '2023-02-15', 22, 7),
    ('Жданов Сергей Александрович', '2024-03-20', 23, 7),
    ('Кузьмин Андрей Валерьевич', '2025-04-25', 21, 7),
    ('Мальцев Роман Николаевич', '2026-05-15', 22, 7),
    ('Наумов Илья Иванович', '2024-06-20', 23, 7),
    ('Осипов Василий Сергеевич', '2021-07-25', 21, 7),
    ('Песков Михаил Петрович', '2020-08-15', 22, 7),
    ('Разумов Анатолий Дмитриевич', '2023-09-20', 23, 7),
    ('Сафронов Александр Валерьевич', '2022-10-25', 21, 7)
ON CONFLICT DO NOTHING;

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Тихонов Денис Сергеевич', '2025-01-10', 24, 8),
    ('Ульянов Андрей Петрович', '2023-02-25', 25, 8),
    ('Филимонов Илья Николаевич', '2021-03-15', 24, 8),
    ('Хабаров Роман Валерьевич', '2024-04-10', 25, 8),
    ('Чесноков Александр Иванович', '2026-05-05', 24, 8),
    ('Шаламов Дмитрий Сергеевич', '2023-06-20', 25, 8),
    ('Эрлих Анатолий Александрович', '2021-07-15', 24, 8),
    ('Юдин Михаил Петрович', '2020-08-10', 25, 8),
    ('Ярцев Сергей Валерьевич', '2021-09-05', 24, 8),
    ('Антонов Денис Андреевич', '2023-10-20', 25, 8)
ON CONFLICT DO NOTHING;

UPDATE employees SET termination_date = '2023-03-31' WHERE full_name = 'Панкратов Илья Николаевич';
UPDATE employees SET termination_date = '2023-07-15' WHERE full_name = 'Филиппов Василий Сергеевич';
UPDATE employees SET termination_date = '2024-01-31' WHERE full_name = 'Орехов Денис Петрович';
UPDATE employees SET termination_date = '2024-06-30' WHERE full_name = 'Царёв Василий Иванович';
UPDATE employees SET termination_date = '2025-02-28' WHERE full_name = 'Разумов Анатолий Дмитриевич';
UPDATE employees SET termination_date = '2025-09-30' WHERE full_name = 'Юрьев Михаил Петрович';
UPDATE employees SET termination_date = '2026-03-31' WHERE full_name = 'Дубровин Александр Иванович';
UPDATE employees SET termination_date = '2026-06-30' WHERE full_name = 'Сафронов Александр Валерьевич';

INSERT INTO employees (full_name, hire_date, profession_id, department_id) VALUES
    ('Петров Артём Сергеевич',    '2025-01-15',  4, 2),
    ('Сидоров Никита Иванович',   '2025-02-10',  6, 2),
    ('Ковалёв Данила Андреевич',  '2025-03-05', 13, 3),
    ('Лебедев Матвей Олегович',   '2025-04-20', 14, 5),
    ('Орлов Александр Дмитриевич','2025-05-12', 17, 6),
    ('Ветров Евгений Игоревич',   '2025-06-08',  1, 1),
    ('Гринёв Антон Валерьевич',   '2025-07-15', 12, 4),
    ('Дроздов Илья Сергеевич',    '2025-08-22', 21, 7),
    ('Жуков Даниил Петрович',     '2025-09-10', 24, 8),
    ('Иванов Степан Андреевич',   '2025-10-05',  4, 2),
    ('Кириллов Роман Олегович',   '2026-01-18',  6, 3),
    ('Лавров Максим Ильич',       '2026-02-12', 13, 5),
    ('Миронов Дмитрий Сергеевич', '2026-03-08', 14, 2),
    ('Никитин Виктор Андреевич',  '2026-04-22', 16, 6),
    ('Осипов Глеб Николаевич',    '2026-05-15', 17, 6),
    ('Павлов Артур Валерьевич',   '2026-06-10', 11, 4),
    ('Родин Кирилл Петрович',     '2026-07-05', 12, 3),
    ('Соколов Ярослав Ильич',     '2026-08-20', 21, 7),
    ('Тарасов Егор Максимович',   '2026-09-12', 24, 8),
    ('Ушаков Никита Олегович',    '2026-10-01',  4, 2)
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

DELETE FROM calculation_results;
DELETE FROM issued_items;
DELETE FROM staffing_plans;

DO $$
    DECLARE
        emp_rec   RECORD;
        norm_rec  RECORD;
    BEGIN
        FOR emp_rec IN
            SELECT e.*
            FROM employees e
            WHERE e.hire_date < '2020-01-01'
              AND (e.termination_date IS NULL OR e.termination_date >= '2020-01-01')
            LOOP
                FOR norm_rec IN
                    SELECT n.*, t.service_life_months
                    FROM norms n
                             JOIN tmc_items t ON t.id = n.tmc_id
                    WHERE n.profession_id = emp_rec.profession_id
                    LOOP
                        INSERT INTO issued_items(
                            employee_id, tmc_item_id, norm_id, quantity,
                            issue_date, expected_return_date, status)
                        VALUES (
                                   emp_rec.id, norm_rec.tmc_id, norm_rec.id, norm_rec.quantity,
                                   '2020-01-01'::date,
                                   '2020-01-01'::date + make_interval(months =>
                                       COALESCE(norm_rec.service_life_months, 12)),
                                   'ISSUED'
                               );
                    END LOOP;
            END LOOP;
    END $$;

DO $$
    DECLARE
        m_start      DATE;
        m_month      INT;
        m_year       INT;
        m_end        DATE;
        emp_rec      RECORD;
        norm_rec     RECORD;
        issued_rec   RECORD;
        transfer_eid INT;
        old_prof     INT;
        new_prof     INT;
        new_dept     INT;
    BEGIN
        FOR m_start IN
            SELECT generate_series('2020-01-01'::date, '2026-10-01'::date, '1 month'::interval)::date
            LOOP
                m_month := EXTRACT(MONTH FROM m_start)::int;
                m_year  := EXTRACT(YEAR  FROM m_start)::int;
                m_end   := (m_start + interval '1 month')::date;

                FOR emp_rec IN
                    SELECT e.*
                    FROM employees e
                    WHERE e.hire_date >= m_start
                      AND e.hire_date <  m_end
                      AND NOT EXISTS (
                        SELECT 1 FROM staffing_plans sp
                        WHERE sp.employee_id = e.id
                          AND sp.action_type = 'HIRE'
                          AND sp.effective_date = e.hire_date
                    )
                    LOOP
                        INSERT INTO staffing_plans(
                            employee_id, action_type, effective_date,
                            new_profession_id, new_department_id)
                        VALUES (emp_rec.id, 'HIRE', emp_rec.hire_date,
                                emp_rec.profession_id, emp_rec.department_id);

                        FOR norm_rec IN
                            SELECT n.*, t.service_life_months
                            FROM norms n
                                     JOIN tmc_items t ON t.id = n.tmc_id
                            WHERE n.profession_id = emp_rec.profession_id
                            LOOP
                                INSERT INTO issued_items(
                                    employee_id, tmc_item_id, norm_id, quantity,
                                    issue_date, expected_return_date, status)
                                VALUES (
                                           emp_rec.id, norm_rec.tmc_id, norm_rec.id, norm_rec.quantity,
                                           emp_rec.hire_date,
                                           emp_rec.hire_date + make_interval(months =>
                                               COALESCE(norm_rec.service_life_months, 12)),
                                           'ISSUED'
                                       );
                            END LOOP;
                    END LOOP;

                FOR emp_rec IN
                    SELECT e.*
                    FROM employees e
                    WHERE e.termination_date >= m_start
                      AND e.termination_date <  m_end
                      AND NOT EXISTS (
                        SELECT 1 FROM staffing_plans sp
                        WHERE sp.employee_id = e.id
                          AND sp.action_type = 'TERMINATE'
                          AND sp.effective_date = e.termination_date
                    )
                    LOOP
                        INSERT INTO staffing_plans(
                            employee_id, action_type, effective_date,
                            old_profession_id, old_department_id)
                        VALUES (emp_rec.id, 'TERMINATE', emp_rec.termination_date,
                                emp_rec.profession_id, emp_rec.department_id);

                        UPDATE issued_items
                        SET status = 'RETURNED',
                            actual_return_date = emp_rec.termination_date
                        WHERE employee_id = emp_rec.id
                          AND status = 'ISSUED';
                    END LOOP;

                FOR transfer_eid IN
                    SELECT e.id
                    FROM employees e
                    WHERE e.hire_date < m_start
                      AND (e.termination_date IS NULL OR e.termination_date >= m_end)
                      AND MOD(ABS(HASHTEXT(e.full_name || m_start::text)), 100) < 1
                      AND NOT EXISTS (
                        SELECT 1 FROM staffing_plans sp
                        WHERE sp.employee_id = e.id
                          AND sp.action_type = 'TRANSFER'
                          AND sp.effective_date >= m_start
                          AND sp.effective_date <  m_end
                    )
                    LOOP
                        SELECT profession_id, department_id INTO old_prof, new_dept
                        FROM employees WHERE id = transfer_eid;

                        new_prof := (ABS(HASHTEXT(transfer_eid || m_start::text)) % 27) + 1;

                        IF new_prof <> old_prof THEN
                            INSERT INTO staffing_plans(
                                employee_id, action_type, effective_date,
                                old_profession_id, old_department_id,
                                new_profession_id, new_department_id)
                            VALUES (transfer_eid, 'TRANSFER', m_start + interval '15 days',
                                    old_prof, new_dept, new_prof, new_dept);

                            UPDATE issued_items ii
                            SET status = 'WRITTEN_OFF',
                                actual_return_date = m_start + interval '15 days'
                            FROM tmc_items t
                            WHERE t.id = ii.tmc_item_id
                              AND ii.employee_id = transfer_eid
                              AND ii.status = 'ISSUED'
                              AND COALESCE(t.service_life_months, 12) >= 3
                              AND NOT EXISTS (
                                SELECT 1 FROM norms n
                                WHERE n.profession_id = new_prof
                                  AND n.tmc_id = ii.tmc_item_id
                            );

                            UPDATE employees SET profession_id = new_prof WHERE id = transfer_eid;

                            FOR norm_rec IN
                                SELECT n.*, t.service_life_months
                                FROM norms n
                                         JOIN tmc_items t ON t.id = n.tmc_id
                                WHERE n.profession_id = new_prof
                                  AND NOT EXISTS (
                                    SELECT 1 FROM issued_items ii
                                    WHERE ii.employee_id = transfer_eid
                                      AND ii.tmc_item_id = n.tmc_id
                                      AND ii.status = 'ISSUED'
                                )
                                LOOP
                                    INSERT INTO issued_items(
                                        employee_id, tmc_item_id, norm_id, quantity,
                                        issue_date, expected_return_date, status)
                                    VALUES (
                                               transfer_eid, norm_rec.tmc_id, norm_rec.id, norm_rec.quantity,
                                               (m_start + interval '15 days')::date,
                                               (m_start + interval '15 days')::date + make_interval(months =>
                                                   COALESCE(norm_rec.service_life_months, 12)),
                                               'ISSUED'
                                           );
                                END LOOP;
                        END IF;
                    END LOOP;

                FOR issued_rec IN
                    SELECT ii.*
                    FROM issued_items ii
                             JOIN tmc_items t ON t.id = ii.tmc_item_id
                    WHERE ii.status = 'ISSUED'
                      AND ii.expected_return_date >= m_start
                      AND ii.expected_return_date <  m_end
                      AND COALESCE(t.service_life_months, 12) >= 3
                    LOOP
                        UPDATE issued_items
                        SET status = 'WRITTEN_OFF',
                            actual_return_date = issued_rec.expected_return_date
                        WHERE id = issued_rec.id;

                        INSERT INTO issued_items(
                            employee_id, tmc_item_id, norm_id, quantity,
                            issue_date, expected_return_date, status)
                        SELECT
                            issued_rec.employee_id,
                            issued_rec.tmc_item_id,
                            issued_rec.norm_id,
                            issued_rec.quantity,
                            issued_rec.expected_return_date,
                            issued_rec.expected_return_date + make_interval(months =>
                                COALESCE(t.service_life_months, 12)),
                            'ISSUED'
                        FROM tmc_items t WHERE t.id = issued_rec.tmc_item_id;
                    END LOOP;

                INSERT INTO calculation_results(
                    tmc_id, required_quantity, period_month, period_year, source)
                SELECT
                    n.tmc_id,
                    GREATEST(1,
                             ROUND(
                                     SUM(n.quantity::numeric * cnt.cnt / n.period_months)
                             )::int
                    ),
                    m_month, m_year, 'NORM_BASE'
                FROM norms n
                         JOIN (
                    SELECT profession_id, COUNT(*)::int AS cnt
                    FROM employees
                    WHERE hire_date <= m_end
                      AND (termination_date IS NULL OR termination_date >= m_end)
                    GROUP BY profession_id
                ) cnt ON cnt.profession_id = n.profession_id
                GROUP BY n.tmc_id
                HAVING SUM(n.quantity::numeric * cnt.cnt / n.period_months) > 0
                ON CONFLICT (tmc_id, period_year, period_month, source) DO UPDATE
                    SET required_quantity = EXCLUDED.required_quantity;

                INSERT INTO calculation_results(
                    tmc_id, required_quantity, period_month, period_year, source)
                SELECT
                    n.tmc_id,
                    SUM(n.quantity)::int,
                    m_month, m_year, 'HIRE'
                FROM norms n
                         JOIN employees e ON e.profession_id = n.profession_id
                WHERE e.hire_date >= m_start AND e.hire_date < m_end
                GROUP BY n.tmc_id
                HAVING SUM(n.quantity) > 0
                ON CONFLICT (tmc_id, period_year, period_month, source) DO UPDATE
                    SET required_quantity = EXCLUDED.required_quantity;

                INSERT INTO calculation_results(
                    tmc_id, required_quantity, period_month, period_year, source)
                SELECT
                    ii.tmc_item_id,
                    SUM(ii.quantity)::int,
                    m_month, m_year, 'REPLACEMENT'
                FROM issued_items ii
                WHERE ii.expected_return_date >= m_start
                  AND ii.expected_return_date <  m_end
                  AND ii.actual_return_date = ii.expected_return_date
                GROUP BY ii.tmc_item_id
                HAVING SUM(ii.quantity) > 0
                ON CONFLICT (tmc_id, period_year, period_month, source) DO UPDATE
                    SET required_quantity = EXCLUDED.required_quantity;

            END LOOP;
    END $$;

SELECT setval('staffing_plans_id_seq',      COALESCE((SELECT MAX(id) FROM staffing_plans), 1));
SELECT setval('issued_items_id_seq',        COALESCE((SELECT MAX(id) FROM issued_items), 1));
SELECT setval('calculation_results_id_seq', COALESCE((SELECT MAX(id) FROM calculation_results), 1));