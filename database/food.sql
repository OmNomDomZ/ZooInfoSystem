create table food
(
    id           serial primary key,
    name         text not null,
    food_type_id int  not null,
    supplier_id  int  not null
);