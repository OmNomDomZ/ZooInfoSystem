-- TODO: скорее всего понадобятся еще скрипты, при добавлении и обновлении чего-то
-- автоматическое создание работников в дочерних таблицах
create or replace function auto_create_child_record()
    returns trigger as
$$
declare
    pos_title varchar;
begin
    select p.title
    into pos_title
    from positions p
    where p.id = new.position_id;

    if pos_title = 'ветеринар' then
        insert into vets (id, license_number)
        values (new.id, (random() * 100000000) :: int);
    elsif pos_title = 'смотритель' then
        insert into keeper (id)
        values (new.id);
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

-- автоматическая фиксация истории перемещений животных по клеткам
create or replace function update_animal_cage_history()
    returns trigger as
$$
begin
    if new.cage_id is distinct from old.cage_id then
        update animal_cage_history
        set end_date = current_date
        where animal_id = old.id
          and end_date is null;

        if new.cage_id is not null then
            insert into animal_cage_history(animal_id, cage_id, start_date, end_date)
            values (new.id, new.cage_id, current_date, null);
        end if;
    end if;
    return new;
end;
$$ language plpgsql;

create trigger trg_update_animal_cage_history
    after update of cage_id
    on animals
    for each row
execute procedure update_animal_cage_history();

-- автоматическая проверка совместимости
drop trigger  if exists trg_check_compatibility on animals;
drop function if exists check_compatibility();

create or replace function check_compatibility()
    returns trigger
    language plpgsql
as
$$
declare
    new_diet          varchar(50);
    conflict_diet     varchar(50);
    conflict_cage_id  int;
begin
    if tg_op = 'insert'
        or (tg_op = 'update' and new.cage_id is distinct from old.cage_id)
    then
        -- 1. определяем диету перемещаемого животного
        select dt.type
        into new_diet
        from animal_types at
                 join diet_types  dt on dt.id = at.diet_type_id
        where at.id = new.animal_type_id;

        -- 2. ищем конфликт:
        --    (а) в самой целевой клетке
        --    (б) в соседних клетках (id-1, id+1)
        for conflict_cage_id in
            select c_id
            from (values (new.cage_id),
                         (new.cage_id - 1),
                         (new.cage_id + 1)) as t(c_id)
            loop
                select dt.type
                into conflict_diet
                from animals a
                         join animal_types at2 on at2.id = a.animal_type_id
                         join diet_types  dt  on dt.id  = at2.diet_type_id
                where a.cage_id = conflict_cage_id
                  and a.id     <> coalesce(new.id,-1)
                  and ((new_diet = 'хищник'     and dt.type = 'травоядное') or
                       (new_diet = 'травоядное' and dt.type = 'хищник'))
                limit 1;

                if conflict_diet is not null then
                    -- конфликт найден → возвращаем в старую клетку
                    new.cage_id := coalesce(old.cage_id, null);
                    raise notice
                        'несовместимость: % (диета=%) нельзя поселить в клетку %, рядом/внутри есть животное с диетой % (клетка %)',
                        new.nickname, new_diet, conflict_cage_id, conflict_diet, conflict_cage_id;
                    return new;
                end if;
            end loop;
    end if;

    return new;
end;
$$;

create trigger trg_check_compatibility
    before insert or update of cage_id
    on animals
    for each row
execute procedure check_compatibility();

-- Автоматическое создание записи о новорожденном
create or replace function auto_create_offspring()
    returns trigger as
$$
declare
    parent_type   int;
    parent_cage   int;
    new_animal_id int;
    random_gender varchar;
begin
    if new.parent_id_1 is not null then
        select animal_type_id, cage_id
        into parent_type, parent_cage
        from animals
        where id = new.parent_id_1;

        if random() > 0.5 then
            random_gender := 'мужской';
        else
            random_gender := 'женский';
        end if;

        insert into animals (nickname, gender, arrival_date, needs_warm_housing, animal_type_id, cage_id)
        values ('Детёныш ' || new.id,
                random_gender,
                new.birth_date,
                false,
                parent_type,
                parent_cage)
        returning id into new_animal_id;

    else
        raise notice 'Не указан parent_id_1, не создаём животное';
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_auto_create_offspring
    after insert
    on birth_records
    for each row
execute procedure auto_create_offspring();
