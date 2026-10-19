DELETE FROM calculation_results
WHERE period_year >= 2024 AND period_year <= 2026;

WITH months AS (
    SELECT
        d::date AS m_date,
        EXTRACT(MONTH FROM d)::int AS m,
        EXTRACT(YEAR FROM d)::int AS y,
        ROW_NUMBER() OVER (ORDER BY d) AS idx
    FROM generate_series('2024-01-01'::date, '2026-09-01'::date, '1 month'::interval) AS d
),
     patterns AS (
         SELECT 'С00000003'::varchar AS code, 100.0 AS base, 2.0 AS trend, 25.0 AS seasonal
         UNION ALL SELECT 'С00000006', 50.0,  0.8,  5.0
         UNION ALL SELECT 'С00000001', 80.0, -0.5, 15.0
         UNION ALL SELECT 'С00000005', 120.0, 1.5, 20.0
         UNION ALL SELECT 'И00000001', 30.0,  0.3,  3.0
         UNION ALL SELECT 'О00000001', 15.0,  0.1,  2.0
     )
INSERT INTO calculation_results (tmc_id, required_quantity, period_month, period_year, calculation_date)
SELECT
    tmc.id,
    GREATEST(1, ROUND(
            p.base + p.trend * m.idx
                + p.seasonal * SIN(m.m::float * 3.14159 / 6)
                + (RANDOM() * 10 - 5)
                )::int),
    m.m,
    m.y,
    m.m_date::timestamp
FROM months m
         CROSS JOIN patterns p
         JOIN tmc_items tmc ON tmc.code = p.code
ORDER BY m.m_date, tmc.id;