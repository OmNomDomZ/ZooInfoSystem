-- должности
create table positions
(
    id    serial primary key,
    title varchar(50) unique not null
);

-- сотрудники
create table employees
(
    id                 serial primary key,
    full_name          varchar(100) not null,
    gender             varchar      not null check (gender in ('мужской', 'женский')),
    hire_date          date         not null check (hire_date <= current_date AND
                                                    hire_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$'),
    birth_date         date         not null check (birth_date <= current_date AND
                                                    birth_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$'),
    -- restrict запрещает операцию, если на объект ссылаются другие объекты или зависимости
    position_id        int          not null references positions (id) on delete restrict,
    salary             numeric(10, 2) check (salary >= 0),
    contact_info       text,
    special_attributes jsonb default '{}'
);

-- профессии (ветеринары, уборщики, смотрители, администраторы)
create table vets
(
    id             int primary key references employees (id) on delete cascade,
    license_number decimal(8, 0),
    specialization varchar(100)
);

create table keeper
(
    id      int primary key references employees (id) on delete cascade,
    section varchar(50)
);

create table janitors
(
    id             int primary key references employees (id) on delete cascade,
    cleaning_shift varchar(20) check (cleaning_shift in ('день', 'ночь')),
    area           varchar(50),
    equipment      varchar(100)
);

create table administrators
(
    id         int primary key references employees (id) on delete cascade,
    department varchar(100),
    phone      varchar(15)
);

-- типы питания
create table diet_types
(
    id   serial primary key,
    type varchar(15) unique not null check (type in ('хищник', 'травоядное', 'всеядное'))
);

-- виды животных
create table animal_types
(
    id           serial primary key,
    type         varchar(50) not null unique,
    diet_type_id int         not null references diet_types (id) on delete cascade
);

-- клетки (жёстко привязаны к виду животного)
create table cages
(
    id             serial primary key,
    animal_type_id int not null references animal_types (id)
        on update cascade on delete restrict,
    capacity       int not null check (capacity > 0)
);

-- животные
create table animals
(
    id                 serial primary key,
    nickname           varchar(50) not null,
    gender             varchar     not null check (gender in ('мужской', 'женский')),
    arrival_date       date        not null check (arrival_date <= current_date AND
                                                   arrival_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$'),
    needs_warm_housing boolean default false,
    animal_type_id     int         not null references animal_types (id) on delete cascade,
    cage_id            int         references cages (id) on delete set null
);

-- сотрудники - виды животных (многие-ко-многим)
create table employees_animal_types
(
    employee_id    int not null references employees (id) on delete cascade,
    animal_type_id int not null references animal_types (id) on delete cascade,
    primary key (employee_id, animal_type_id)
);

-- сотрудники - клетки (многие-ко-многим)
create table employees_cages_access
(
    employee_id int not null references employees (id) on delete cascade,
    cage_id     int not null references cages (id) on delete cascade,
    primary key (employee_id, cage_id)
);


-- типы еды
create table food_types
(
    id   serial primary key,
    type varchar(50) unique not null
);

-- еда
create table food
(
    id                     serial primary key,
    name                   varchar(100) unique not null,
    food_type_id           int                 not null references food_types (id) on delete cascade,
    is_produced_internally boolean default false
);

-- рацион животных
create table rations
(
    id           serial primary key,
    animal_id    int  not null references animals (id) on delete cascade,
    food_id      int  not null references food (id) on delete cascade,
    amount       numeric(6, 2) check (amount > 0),
    feeding_time time not null
);

-- поставщики еды
create table suppliers
(
    id       serial primary key,
    name     varchar(100) unique not null,
    contacts text                not null
);

-- поставщики -- еда (многие-ко-многим)
create table suppliers_food
(
    id            serial primary key,
    supplier_id   int            not null references suppliers (id) on delete cascade,
    food_id       int            not null references food (id) on delete cascade,
    delivery_date date           not null check (delivery_date <= current_date AND
                                                 delivery_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$'),
    quantity      numeric(10, 2) not null check (quantity > 0),
    price         numeric(10, 2) not null check (price >= 0)
);

-- зоопарки
create table zoos
(
    id       serial primary key,
    name     varchar(100) unique not null,
    address  text,
    contacts text
);

-- перемещения животных
create table transfers
(
    id                 serial primary key,
    animal_id          int  not null references animals (id) on delete cascade,
    reason             text not null,
    destination_zoo_id int  references zoos (id) on delete set null,
    transfer_date      date not null check (transfer_date <= current_date AND
                                            transfer_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$')
);

-- медицинские записи
create table medical_records
(
    id           serial primary key,
    animal_id    int           not null references animals (id) on delete cascade,
    birth_date   date          not null check (birth_date <= current_date AND
                                               birth_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$'),
    weight       numeric(6, 2) not null check (weight > 0),
    height       numeric(6, 2) not null check (height > 0),
    vaccinations text,
    illnesses    text,
    checkup_date date          not null check (checkup_date <= current_date AND
                                               checkup_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$')
);

-- записи о рождении
create table birth_records
(
    id          serial primary key,
    child_id    int references animals (id) on delete cascade,
    parent_id_1 int references animals (id),
    parent_id_2 int references animals (id),
    birth_date  date        not null check (birth_date <= current_date AND
                                            birth_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$'),
    status      varchar(50) not null
        check (status in ('оставлен', 'обменен', 'отдан'))
);

-- история перемещений по клеткам
create table animal_cage_history
(
    id         serial primary key,
    animal_id  int  not null references animals (id) on delete cascade,
    cage_id    int  not null references cages (id) on delete cascade,
    start_date date not null check (start_date <= current_date AND start_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$'),
    end_date   date check (end_date::text ~ '^[0-9]{4}-[0-9]{2}-[0-9]{2}$')
);