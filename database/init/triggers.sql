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
create or replace function check_compatibility()
    returns trigger as
$$
declare
    diet1 varchar(50);
    diet2 varchar(50);
begin
    select dt.type into diet1
    from animal_types at
             join diet_types dt on at.diet_type_id = dt.id
    where at.id = new.animal_type_id_1;

    select dt.type into diet2
    from animal_types at
             join diet_types dt on at.diet_type_id = dt.id
    where at.id = new.animal_type_id_2;

    if ((diet1 = 'хищник' and diet2 = 'травоядное')
        or (diet1 = 'травоядное' and diet2 = 'хищник')) then
        new.compatible := false;
    elsif ((diet1 = 'травоядное' and diet2 = 'всеядное')
        or (diet1 = 'всеядное' and diet2 = 'травоядное')) then
        new.compatible := false;
    else
        new.compatible := true;
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_check_compatibility
    before insert or update
    on compatibility
    for each row
execute procedure check_compatibility();

-- автоматическое заполнение совместимости
create or replace function auto_fill_compatibility()
returns trigger as
$$
begin
    delete from compatibility
     where animal_type_id_1 = new.id
        or animal_type_id_2 = new.id;

    insert into compatibility (animal_type_id_1, animal_type_id_2, compatible)
    select new.id, at.id, null
      from animal_types at
     where at.id <> new.id;

    insert into compatibility (animal_type_id_1, animal_type_id_2, compatible)
    select at.id, new.id, null
      from animal_types at
     where at.id <> new.id;

    return new;
end;
$$ language plpgsql;

create trigger trg_auto_fill_compatibility
after insert or update
on animal_types
for each row
execute procedure auto_fill_compatibility();

-- Автоматическое создание записи о новорожденном
create or replace function auto_create_offspring()
    returns trigger as
$$
declare
    parent_type   int;
    parent_cage int;
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
after insert on birth_records
for each row
execute procedure auto_create_offspring();
