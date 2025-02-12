create table employees (
    id serial primary key,
    full_name text not null,
    position_id text not null, -- должность
    salary numeric not null,
    contact_info text,
    special_attributes jsonb -- уникальные характеристики
);