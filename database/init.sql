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
    hire_date          date         not null,
    birth_date         date         not null,
    position_id        int          not null references positions (id) on delete restrict,
    salary             numeric(10, 2) check (salary >= 0),
    contact_info       text,
    special_attributes jsonb default '{}'
);

-- профессии (ветеринары, уборщики, смотрители, администраторы)
create table vets
(
    id             int primary key references employees (id) on delete cascade,
    license_number varchar(50) unique not null
);

create table keeper
(
    id int primary key references employees (id) on delete cascade
);

create table janitors
(
    id             int primary key references employees (id) on delete cascade,
    cleaning_shift varchar(20) check (cleaning_shift in ('день', 'ночь')),
    area           varchar(100)
);

create table administrators
(
    id         int primary key references employees (id) on delete cascade,
    department varchar(100) not null
);

-- типы питания
create table diet_types
(
    id   serial primary key,
    type varchar(50) unique not null check (type in ('хищник', 'травоядное', 'всеядное'))
);

-- виды животных
create table animal_types
(
    id           serial primary key,
    type         varchar(100) not null unique,
    diet_type_id int          not null references diet_types (id) on delete cascade
);

-- клетки
create table cages
(
    id           serial primary key,
    climate_zone varchar(100),
    size         numeric(6, 2) check (size > 0),
    temperature  numeric(4, 1),
    max_num      int check (max_num > 0)
);

-- животные
create table animals
(
    id                 serial primary key,
    nickname           varchar(50) not null,
    gender             varchar     not null check (gender in ('мужской', 'женский')),
    arrival_date       date        not null,
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
create table supplier_food
(
    id          serial primary key,
    supplier_id int not null references suppliers (id) on delete cascade,
    food_id     int not null references food (id) on delete cascade
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
    id            serial primary key,
    animal_id     int          not null references animals (id) on delete cascade,
    reason        text         not null,
    destination_zoo_id int references zoos(id) on delete set null,
    transfer_date date         not null
);

-- медицинские записи
create table medical_records
(
    id           serial primary key,
    animal_id    int  not null references animals (id) on delete cascade,
    age          int check (age >= 0),
    weight       numeric(6, 2) check (weight > 0),
    height       numeric(6, 2) check (height > 0),
    vaccinations text,
    illnesses    text,
    checkup_date date not null
);

-- совместимости животных
create table compatibility
(
    animal_type_id_1 int     not null references animal_types (id) on delete cascade,
    animal_type_id_2 int     not null references animal_types (id) on delete cascade,
    compatible       boolean not null,
    primary key (animal_type_id_1, animal_type_id_2)
);

-- записи о рождении
create table birth_records
(
    id          serial primary key,
    parent_id_1 int  references animals (id) on delete set null,
    parent_id_2 int  references animals (id) on delete set null,
    birth_date  date not null,
    status      varchar(50) check (status in ('оставлен', 'обменен', 'отдан'))
);

-- история перемещений по клеткам
create table animal_cage_history
(
    id         serial primary key,
    animal_id  int  not null references animals (id) on delete cascade,
    cage_id    int  not null references cages (id) on delete cascade,
    start_date date not null,
    end_date   date
);

-- информацию о каждой поставке
create table supply_history
(
    id            serial primary key,
    supplier_id   int  not null references suppliers (id) on delete cascade,
    food_id       int  not null references food (id) on delete cascade,
    delivery_date date not null,
    quantity      numeric(10, 2) check (quantity > 0),
    price         numeric(10, 2) check (price >= 0)
);