create table ration
(
    id        serial primary key,
    animal_id int     not null,
    food_id   int     not null,
    amount   numeric not null, -- количество
    feeding_time   time    not null -- время кормления
);