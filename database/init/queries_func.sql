-- 1) Общий список сотрудников
CREATE OR REPLACE FUNCTION fn_employees_overview()
    RETURNS TABLE
            (
                fio             varchar,
                gender          varchar,
                hire_date       date,
                work_duration   integer,
                birth_date      date,
                salary          numeric,
                total_employees bigint
            )
AS
$$
SELECT e.full_name                  AS fio,
       e.gender,
       e.hire_date,
       (current_date - e.hire_date) AS work_duration,
       e.birth_date,
       e.salary,
       count(*) OVER ()             AS total_employees
FROM employees e
ORDER BY work_duration DESC, e.gender, e.birth_date, e.salary DESC;
$$
    LANGUAGE sql;



-- 2) Сотрудники, ответственные за вид (параметр p_type)
CREATE OR REPLACE FUNCTION fn_employees_by_animal_type(p_type varchar)
    RETURNS TABLE
            (
                fio             varchar,
                animal_type     varchar,
                total_employees bigint
            )
AS
$$
SELECT e.full_name      AS fio,
       at.type          AS animal_type,
       count(*) OVER () AS total_employees
FROM employees e
         JOIN employees_animal_types eat ON e.id = eat.employee_id
         JOIN animal_types at ON eat.animal_type_id = at.id
WHERE at.type = p_type
ORDER BY e.full_name;
$$ LANGUAGE sql;



-- 3) Сотрудники с доступом к виду (тот же p_type)
CREATE OR REPLACE FUNCTION fn_employees_with_cage_access(p_type varchar)
    RETURNS TABLE
            (
                fio             varchar,
                animal_type     varchar,
                total_employees bigint
            )
AS
$$
WITH matched AS (SELECT DISTINCT e.id, e.full_name, at.type
                 FROM employees e
                          JOIN employees_cages_access eca ON e.id = eca.employee_id
                          JOIN cages c ON eca.cage_id = c.id
                          JOIN animals a ON c.id = a.cage_id
                          JOIN animal_types at ON a.animal_type_id = at.id
                 WHERE at.type = p_type)
SELECT m.full_name                    AS fio,
       m.type                         AS animal_type,
       (SELECT count(*) FROM matched) AS total_employees
FROM matched m
ORDER BY m.full_name;
$$ LANGUAGE sql;



-- 4) Все животные в клетке (параметр p_cage_id)
CREATE OR REPLACE FUNCTION fn_animals_in_cage(p_cage int)
    RETURNS TABLE
            (
                nickname      varchar,
                gender        varchar,
                arrival_date  date,
                age_years     int,
                weight        numeric,
                height        numeric,
                total_animals bigint
            )
AS
$$
SELECT a.nickname,
       a.gender,
       a.arrival_date,
       extract(year FROM age(current_date, mr.birth_date))::int AS age_years,
       coalesce(mr.weight, 0)                                   AS weight,
       coalesce(mr.height, 0)                                   AS height,
       count(*) OVER ()                                         AS total_animals
FROM animals a
         JOIN animal_cage_history ach ON a.id = ach.animal_id
         LEFT JOIN medical_records mr ON a.id = mr.animal_id
WHERE ach.cage_id = p_cage
ORDER BY a.gender;
$$ LANGUAGE sql;



-- 5) Животные, нуждающиеся в тепле (и опционально по виду p_type)
CREATE OR REPLACE FUNCTION fn_animals_needing_heat(p_type varchar default NULL)
    RETURNS TABLE
            (
                nickname      varchar,
                gender        varchar,
                arrival_date  date,
                animal_type   varchar,
                total_animals bigint
            )
AS
$$
SELECT a.nickname,
       a.gender,
       a.arrival_date,
       at.type          AS animal_type,
       count(*) OVER () AS total_animals
FROM animals a
         JOIN animal_types at ON a.animal_type_id = at.id
WHERE a.needs_warm_housing
  AND (p_type IS NULL OR at.type = p_type)
ORDER BY a.nickname;
$$ LANGUAGE sql;



-- 6) Животные с прививкой p_vaccine
CREATE OR REPLACE FUNCTION fn_animals_by_vaccine(p_vaccine varchar)
    RETURNS TABLE
            (
                nickname      varchar,
                gender        varchar,
                vaccinations  text,
                illnesses     text,
                arrival_date  date,
                total_animals bigint
            )
AS
$$
SELECT a.nickname,
       a.gender,
       mr.vaccinations,
       mr.illnesses,
       a.arrival_date,
       count(*) OVER () AS total_animals
FROM animals a
         JOIN medical_records mr ON a.id = mr.animal_id
WHERE mr.vaccinations ILIKE '%' || p_vaccine || '%'
ORDER BY a.arrival_date;
$$ LANGUAGE sql;



-- 7) Совместимые по рациону с видом p_type
CREATE OR REPLACE FUNCTION fn_compatible_animals(p_type varchar)
    RETURNS TABLE
            (
                nickname           varchar,
                animal_type        varchar,
                diet_type          varchar,
                needs_warm_housing boolean
            )
AS
$$
WITH spec AS (SELECT at.id AS animal_type_id, dt.type AS diet_type
              FROM animal_types at
                       JOIN diet_types dt ON at.diet_type_id = dt.id
              WHERE at.type = p_type)
SELECT DISTINCT a.nickname,
                at.type AS animal_type,
                dt.type AS diet_type,
                a.needs_warm_housing
FROM animals a
         JOIN animal_types at ON a.animal_type_id = at.id
         JOIN diet_types dt ON at.diet_type_id = dt.id
         CROSS JOIN spec s
WHERE dt.type = s.diet_type
ORDER BY a.nickname;
$$ LANGUAGE sql;



-- 8) Поставщики по периоду (p_from, p_to)
CREATE OR REPLACE FUNCTION fn_suppliers_by_period(p_from date, p_to date)
    RETURNS TABLE
            (
                supplier_name   varchar,
                contacts        text,
                food_id         int,
                delivery_date   date,
                quantity        numeric,
                price           numeric,
                total_suppliers bigint
            )
AS
$$
SELECT s.name           AS supplier_name,
       s.contacts,
       sf.food_id,
       sf.delivery_date,
       sf.quantity,
       sf.price,
       count(*) OVER () AS total_suppliers
FROM suppliers s
         JOIN suppliers_food sf ON s.id = sf.supplier_id
WHERE sf.delivery_date BETWEEN p_from AND p_to
ORDER BY sf.delivery_date;
$$ LANGUAGE sql;



-- 9) Рационы, производимые внутри
CREATE OR REPLACE FUNCTION fn_internal_food()
    RETURNS TABLE
            (
                food_name              varchar,
                is_produced_internally boolean,
                supply_count           bigint,
                total_quantity         numeric
            )
AS
$$
SELECT f.name            AS food_name,
       f.is_produced_internally,
       count(sf.food_id) AS supply_count,
       sum(sf.quantity)  AS total_quantity
FROM food f
         LEFT JOIN suppliers_food sf ON f.id = sf.food_id
WHERE f.is_produced_internally
GROUP BY f.name, f.is_produced_internally;
$$ LANGUAGE sql;



-- 10) Животные, требующие типа корма p_food_type
CREATE OR REPLACE FUNCTION fn_animals_by_food_type(p_food_type varchar)
    RETURNS TABLE
            (
                nickname      varchar,
                gender        varchar,
                food_type     varchar,
                total_animals bigint
            )
AS
$$
SELECT a.nickname,
       a.gender,
       ft.type          AS food_type,
       count(*) OVER () AS total_animals
FROM animals a
         JOIN rations r ON a.id = r.animal_id
         JOIN food f ON r.food_id = f.id
         JOIN food_types ft ON f.food_type_id = ft.id
WHERE ft.type = p_food_type
ORDER BY a.nickname;
$$ LANGUAGE sql;



-- 11) Полная информация по животным (опционально по виду p_type)
CREATE OR REPLACE FUNCTION fn_animal_full_info(p_type varchar default NULL)
    RETURNS TABLE
            (
                id              int,
                nickname        varchar,
                gender          varchar,
                arrival_date    date,
                birth_date      date,
                age_years       int,
                weight          numeric,
                height          numeric,
                vaccinations    text,
                illnesses       text,
                offspring_count bigint,
                total_animals   bigint
            )
AS
$$
SELECT a.id,
       a.nickname,
       a.gender,
       a.arrival_date,
       mr.birth_date,
       CASE
           WHEN mr.birth_date IS NOT NULL
               THEN extract(year FROM age(current_date, mr.birth_date))::int
           ELSE NULL END             AS age_years,
       mr.weight,
       mr.height,
       mr.vaccinations,
       mr.illnesses,
       (SELECT count(*)
        FROM birth_records br
        WHERE br.parent_id_1 = a.id
           OR br.parent_id_2 = a.id) AS offspring_count,
       count(*) OVER ()              AS total_animals
FROM animals a
         LEFT JOIN medical_records mr ON a.id = mr.animal_id
WHERE p_type IS NULL
   OR a.animal_type_id = (SELECT id
                          FROM animal_types
                          WHERE type = p_type)
ORDER BY a.nickname;
$$ LANGUAGE sql;



-- 12) Перспективные родители в жёстком периоде
CREATE OR REPLACE FUNCTION fn_potential_parents(
    p_period_start DATE
)
    RETURNS TABLE(
                     id       INT,
                     nickname VARCHAR,
                     species  VARCHAR,
                     cage_id  INT
                 )
    LANGUAGE sql AS
$$
SELECT
    a.id,
    a.nickname,
    t.type    AS species,
    a.cage_id
FROM animals a
         JOIN animal_types t ON a.animal_type_id = t.id
WHERE
  -- в одной клетке хотя бы ещё один того же вида
    EXISTS (
        SELECT 1
        FROM animals a2
        WHERE a2.animal_type_id = a.animal_type_id
          AND a2.cage_id        = a.cage_id
          AND a2.id <> a.id
    )
  -- нет детей более свежих, чем p_period_start - 3 месяца
  AND COALESCE(
              ( SELECT MAX(br.birth_date)
                FROM birth_records br
                WHERE br.parent_id_1 = a.id
                   OR br.parent_id_2 = a.id
              ),
              '1900-01-01'::date
      )
    <= (p_period_start - INTERVAL '3 months')
ORDER BY a.cage_id, a.animal_type_id, a.id;
$$;




-- 13) Зоопарки-партнёры по обменам (опционально p_type)
CREATE OR REPLACE FUNCTION fn_partner_zoos(p_type varchar default NULL)
    RETURNS TABLE
            (
                zoo_name   varchar,
                address    text,
                contacts   text,
                total_zoos bigint
            )
AS
$$
SELECT z.name           AS zoo_name,
       z.address,
       z.contacts,
       count(*) OVER () AS total_zoos
FROM transfers t
         JOIN zoos z ON t.destination_zoo_id = z.id
         JOIN animals a ON t.animal_id = a.id
WHERE p_type IS NULL
   OR a.animal_type_id = (SELECT id
                          FROM animal_types
                          WHERE type = p_type)
GROUP BY z.id, z.name, z.address, z.contacts
ORDER BY z.name;
$$ LANGUAGE sql;
