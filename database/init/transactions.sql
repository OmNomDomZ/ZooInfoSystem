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
