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
        values (new.id, 'default_license');
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
        where animal_id = old.id and end_date is null;

        if new.cage_id is not null then
            insert into animal_cage_history(animal_id, cage_id, start_date, end_date)
            values (new.id, new.cage_id, current_date, null);
        end if;
    end if;
    return new;
end;
$$ language plpgsql;

create trigger trg_update_animal_cage_history
after update of cage_id on animals
for each row
execute procedure update_animal_cage_history();