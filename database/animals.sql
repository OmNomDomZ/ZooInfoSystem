create table animals
(
    id         serial primary key,
    nickname   text    not null,
    animal_type       int    not null,
    age        int     not null,
    weight     numeric not null,
    height     numeric not null,
    diet_id text    not null, -- подвид (травоядное/хищник)
    climate_zone    text    not null, -- климатическая зона (нужно переводить в отапливаемое помещение или нет)
    cage_id    int     not null
);