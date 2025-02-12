-- потомство
create table birth_records
(
    id            serial primary key,
    parents_id_1  int  not null,
    parents_id_2  int  not null,
    birth_date  date not null,
    status        text not null -- Оставлен в зоопарке / передан другому зоопарку
);