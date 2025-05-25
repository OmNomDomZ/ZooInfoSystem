CREATE OR REPLACE FUNCTION save_animal_with_birth(
    p_nickname            TEXT,
    p_gender              TEXT,
    p_arrival_date        DATE,
    p_needs_warm_housing  BOOLEAN,
    p_animal_type_id      INTEGER,
    p_cage_id             INTEGER,
    p_parent1_id          INTEGER,
    p_parent2_id          INTEGER
) RETURNS INTEGER
    LANGUAGE plpgsql AS $$
DECLARE
    new_id INTEGER;
BEGIN
    RAISE NOTICE '[save_birth] starting for parents %, %', p_parent1_id, p_parent2_id;

    -- вставка в animals
    INSERT INTO animals(
        nickname, gender, arrival_date,
        needs_warm_housing, animal_type_id, cage_id
    ) VALUES (
                 p_nickname, p_gender, p_arrival_date,
                 p_needs_warm_housing, p_animal_type_id, p_cage_id
             )
    RETURNING id INTO new_id;
    RAISE NOTICE '[save_birth] inserted animal id=%', new_id;

    -- вставка в birth_records
    INSERT INTO birth_records(
        child_id, parent_id_1, parent_id_2,
        birth_date, status
    ) VALUES (
                 new_id, p_parent1_id, p_parent2_id,
                 p_arrival_date, 'оставлен'
             );
    RAISE NOTICE '[save_birth] inserted birth_records for child %', new_id;

    -- вернуть сгенерированный id
    RETURN new_id;
EXCEPTION WHEN OTHERS THEN
    RAISE NOTICE '[save_birth] error: %', SQLERRM;
    RAISE;
END;
$$;

-- BEGIN…EXCEPTION создаёт внутренний savepoint: при любой ошибке в блоке (INSERT или UPDATE)
-- PostgreSQL автоматически откатит все изменения внутри этого блока (включая вставку в employees),
-- а затем RAISE пробросит исключение наружу. Явные SAVEPOINT/ROLLBACK TO внутри функций не поддерживаются,
-- поэтому этот механизм — стандартный способ вложенного отката в PL/pgSQL.

-- ========================================
-- ВЕТЕРИНАР
-- ========================================
CREATE OR REPLACE FUNCTION add_vet(
    p_full_name       VARCHAR(100),
    p_gender          VARCHAR,
    p_hire_date       DATE,
    p_birth_date      DATE,
    p_position_id     INT,
    p_salary          NUMERIC(10,2),
    p_contact_info    TEXT,
    p_license_number  DECIMAL(8,0),
    p_specialization  VARCHAR(100)
) RETURNS INT
    LANGUAGE plpgsql
AS $func$
DECLARE
    emp_id INT;
BEGIN
    BEGIN
        INSERT INTO employees(full_name, gender, hire_date, birth_date,
                              position_id, salary, contact_info)
        VALUES (p_full_name, p_gender, p_hire_date, p_birth_date,
                p_position_id, p_salary, p_contact_info)
        RETURNING id INTO emp_id;

        UPDATE vets SET license_number = p_license_number, specialization = p_specialization WHERE id = emp_id;
    EXCEPTION WHEN OTHERS THEN
        RAISE;
    END;

    RETURN emp_id;
END;
$func$;


-- ========================================
-- СМОТРИТЕЛЬ
-- ========================================
CREATE OR REPLACE FUNCTION add_keeper(
    p_full_name    VARCHAR(100),
    p_gender       VARCHAR,
    p_hire_date    DATE,
    p_birth_date   DATE,
    p_position_id  INT,
    p_salary       NUMERIC(10,2),
    p_contact_info TEXT,
    p_section      VARCHAR(100)
) RETURNS INT
    LANGUAGE plpgsql
AS $func$
DECLARE
    emp_id INT;
BEGIN
    BEGIN
        INSERT INTO employees(full_name, gender, hire_date, birth_date,
                              position_id, salary, contact_info)
        VALUES (p_full_name, p_gender, p_hire_date, p_birth_date,
                p_position_id, p_salary, p_contact_info)
        RETURNING id INTO emp_id;

        UPDATE keeper SET section = p_section WHERE id = emp_id;
    EXCEPTION WHEN OTHERS THEN
        RAISE;
    END;

    RETURN emp_id;
END;
$func$;


-- ========================================
-- УБОРЩИК
-- ========================================
CREATE OR REPLACE FUNCTION add_janitor(
    p_full_name       VARCHAR(100),
    p_gender          VARCHAR,
    p_hire_date       DATE,
    p_birth_date      DATE,
    p_position_id     INT,
    p_salary          NUMERIC(10,2),
    p_contact_info    TEXT,
    p_cleaning_shift  VARCHAR(20),
    p_area            VARCHAR(100),
    p_equipment       VARCHAR(100)
) RETURNS INT
    LANGUAGE plpgsql
AS $func$
DECLARE
    emp_id INT;
BEGIN
    BEGIN
        INSERT INTO employees(full_name, gender, hire_date, birth_date,
                              position_id, salary, contact_info)
        VALUES (p_full_name, p_gender, p_hire_date, p_birth_date,
                p_position_id, p_salary, p_contact_info)
        RETURNING id INTO emp_id;

        UPDATE janitors SET cleaning_shift = p_cleaning_shift, area = p_area, equipment = p_equipment WHERE id = emp_id;
    EXCEPTION WHEN OTHERS THEN
        RAISE;
    END;

    RETURN emp_id;
END;
$func$;


-- ========================================
-- АДМИНИСТРАТОР
-- ========================================
CREATE OR REPLACE FUNCTION add_administrator(
    p_full_name    VARCHAR(100),
    p_gender       VARCHAR,
    p_hire_date    DATE,
    p_birth_date   DATE,
    p_position_id  INT,
    p_salary       NUMERIC(10,2),
    p_contact_info TEXT,
    p_department   VARCHAR(100),
    p_phone        VARCHAR(15)
) RETURNS INT
    LANGUAGE plpgsql
AS $func$
DECLARE
    emp_id INT;
BEGIN
    BEGIN
        INSERT INTO employees(full_name, gender, hire_date, birth_date,
                              position_id, salary, contact_info)
        VALUES (p_full_name, p_gender, p_hire_date, p_birth_date,
                p_position_id, p_salary, p_contact_info)
        RETURNING id INTO emp_id;

        UPDATE administrators SET department = p_department, phone = p_phone WHERE id = emp_id;
    EXCEPTION WHEN OTHERS THEN
        RAISE;
    END;

    RETURN emp_id;
END;
$func$;