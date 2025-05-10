-- === автоматическое создание записи в дочерних таблицах при добавлении сотрудника ===
drop trigger if exists trg_auto_create_child on employees;
drop function if exists auto_create_child_record();

create or replace function auto_create_child_record()
    returns trigger as
$$
declare
    pos_title varchar;
begin
    select p.title into pos_title from positions p where p.id = new.position_id;

    if pos_title = 'ветеринар' then
        insert into vets (id, license_number)
        values (new.id, (random() * 100000000)::int);
    elsif pos_title = 'смотритель' then
        insert into keeper (id) values (new.id);
    elsif pos_title = 'уборщик' then
        insert into janitors (id, cleaning_shift, area)
        values (new.id, 'день', 'default_area');
    elsif pos_title = 'администратор' then
        insert into administrators (id, department)
        values (new.id, 'default_department');
    else
        raise exception 'Неизвестная должность: %', pos_title;
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_auto_create_child
    after insert
    on employees
    for each row
execute procedure auto_create_child_record();


-- === автоматическое ведение истории клеток при смене cage_id ===
drop trigger if exists trg_update_animal_cage_history on animals;
drop function if exists update_animal_cage_history();

CREATE OR REPLACE FUNCTION update_animal_cage_history()
    RETURNS trigger AS
$$
BEGIN
    -- при создании нового животного
    IF TG_OP = 'INSERT' THEN
        IF NEW.cage_id IS NOT NULL THEN
            INSERT INTO animal_cage_history(animal_id, cage_id, start_date, end_date)
            VALUES (NEW.id, NEW.cage_id, CURRENT_DATE, NULL);
        END IF;

        -- при смене клетки
    ELSIF TG_OP = 'UPDATE' THEN
        IF NEW.cage_id IS DISTINCT FROM OLD.cage_id THEN
            -- закрываем предыдущую запись
            UPDATE animal_cage_history
            SET end_date = CURRENT_DATE
            WHERE animal_id = OLD.id
              AND end_date IS NULL;

            -- создаём новую, если клетка не NULL
            IF NEW.cage_id IS NOT NULL THEN
                INSERT INTO animal_cage_history(animal_id, cage_id, start_date, end_date)
                VALUES (NEW.id, NEW.cage_id, CURRENT_DATE, NULL);
            END IF;
        END IF;
    END IF;

    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

create trigger trg_update_animal_cage_history
    after insert or update of cage_id
    on animals
    for each row
execute procedure update_animal_cage_history();


-- === проверка совместимости животного и клетки ===
drop trigger if exists trg_animals_check_cage on animals;
drop function if exists validate_animal_cage();

create or replace function validate_animal_cage()
    returns trigger as
$$
declare
    allowed_type int;
begin
    select animal_type_id into allowed_type from cages where id = new.cage_id;

    if allowed_type is null then
        raise exception 'Клетка % не найдена', new.cage_id;
    end if;

    if allowed_type <> new.animal_type_id then
        raise exception
            'Нельзя заселить животное типа % в клетку %, предназначенную для типа %',
            new.animal_type_id, new.cage_id, allowed_type;
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_animals_check_cage
    before insert or update of cage_id
    on animals
    for each row
execute function validate_animal_cage();
