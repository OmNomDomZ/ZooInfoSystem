create table cages
(
    id            serial primary key,
    size          numeric not null,
    temperature   decimal not null,
    zone_id       int     not null,
    compatibility jsonb   not null -- совместимость (например, запрещенные соседи)
);