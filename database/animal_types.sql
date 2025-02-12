create table animal_types
(
    id        serial primary key,
    name text  not null, -- название вида
    food_type_id int not null -- вид пищи
);