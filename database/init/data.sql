-- Должности сотрудников
insert into positions (title)
values ('ветеринар'),
       ('смотритель'),
       ('уборщик'),
       ('администратор');

-- Типы питания животных
insert into diet_types (type)
values ('хищник'),
       ('травоядное'),
       ('всеядное');

-- Типы еды
insert into food_types (type)
values ('растительный'),
       ('мясо'),
       ('живой корм'),
       ('комбикорм');

-- Зоопарки-партнёры
insert into zoos (name, address, contacts)
values ('Московский зоопарк', 'Москва, Россия', '+7 495 123-45-67'),
       ('Берлинский зоопарк', 'Берлин, Германия', '+49 30 1234567'),
       ('Лондонский зоопарк', 'Лондон, Великобритания', '+44 20 1234 5678');

-- Виды животных и их тип питания
insert into animal_types (type, diet_type_id)
values ('Лев', (select id from diet_types where type = 'хищник')),
       ('Слон', (select id from diet_types where type = 'травоядное')),
       ('Медведь', (select id from diet_types where type = 'всеядное')),
       ('Волк', (select id from diet_types where type = 'хищник')),
       ('Жираф', (select id from diet_types where type = 'травоядное'));

-- Клетки
insert into cages (climate_zone, size, temperature, max_num)
values ('Саванна', 50.0, 30.0, 3),
       ('Тропики', 100.0, 28.0, 2),
       ('Тайга', 70.0, -5.0, 2),
       ('Лес', 60.0, 5.0, 3),
       ('Степь', 90.0, 20.0, 3);

-- Животные
insert into animals (nickname, gender, arrival_date, needs_warm_housing, animal_type_id, cage_id)
values ('Симба', 'мужской', '2022-05-10', true, (select id from animal_types where type = 'Лев'), 1),
       ('Дамбо', 'мужской', '2021-09-15', false, (select id from animal_types where type = 'Слон'), 2),
       ('Миша', 'мужской', '2020-12-20', false, (select id from animal_types where type = 'Медведь'), 3),
       ('Альфа', 'женский', '2023-01-05', true, (select id from animal_types where type = 'Волк'), 4),
       ('Жорик', 'мужской', '2022-07-30', false, (select id from animal_types where type = 'Жираф'), 5),
       ('Тигра', 'мужской', '2022-09-22', true, (select id from animal_types where type = 'Лев'), 1),
       ('Балу', 'мужской', '2020-05-19', false, (select id from animal_types where type = 'Медведь'), 3),
       ('Луна', 'женский', '2021-11-30', true, (select id from animal_types where type = 'Волк'), 4),
       ('Гром', 'мужской', '2023-03-17', false, (select id from animal_types where type = 'Жираф'), 5),
       ('Тайсон', 'мужской', '2023-06-21', false, (select id from animal_types where type = 'Лев'), 1);

-- Сотрудники
insert into employees
(full_name, gender, hire_date, birth_date, position_id, salary, contact_info)
values ('Иван Петров', 'мужской', '2020-06-01', '1990-04-15',
        (select id from positions where title = 'ветеринар'), 80000, 'ivan@example.com'),
       ('Мария Сидорова', 'женский', '2019-03-12', '1985-07-22',
        (select id from positions where title = 'смотритель'), 60000, 'maria@example.com'),
       ('Андрей Смирнов', 'мужской', '2021-11-25', '1995-09-10',
        (select id from positions where title = 'уборщик'), 40000, 'andrey@example.com'),
       ('Елена Козлова', 'женский', '2018-08-19', '1982-06-30',
        (select id from positions where title = 'администратор'), 90000, 'elena@example.com'),
       ('Артем Васильев', 'мужской', '2022-07-01', '1988-11-13',
        (select id from positions where title = 'ветеринар'), 75000, 'artem@example.com'),
       ('Ольга Иванова', 'женский', '2017-06-25', '1980-05-17',
        (select id from positions where title = 'смотритель'), 65000, 'olga@example.com'),
       ('Сергей Волков', 'мужской', '2020-09-30', '1992-07-19',
        (select id from positions where title = 'уборщик'), 42000, 'sergey@example.com'),
       ('Наталья Белова', 'женский', '2015-05-10', '1978-04-22',
        (select id from positions where title = 'администратор'), 95000, 'natalia@example.com'),
       ('Максим Соловьев', 'мужской', '2023-02-15', '1998-12-30',
        (select id from positions where title = 'ветеринар'), 72000, 'maxim@example.com'),
       ('Дарья Орлова', 'женский', '2021-08-11', '1991-09-25',
        (select id from positions where title = 'смотритель'), 58000, 'daria@example.com');

-- Еда
insert into food (name, food_type_id, is_produced_internally)
values ('Мясо говяжье', (select id from food_types where type = 'мясо'), false),
       ('Фрукты', (select id from food_types where type = 'растительный'), true),
       ('Комбикорм', (select id from food_types where type = 'комбикорм'), true),
       ('Кролики', (select id from food_types where type = 'живой корм'), false),
       ('Овощи', (select id from food_types where type = 'растительный'), true),
       ('Рыба', (select id from food_types where type = 'мясо'), false),
       ('Зерно', (select id from food_types where type = 'комбикорм'), true);

-- Рацион
insert into rations (animal_id, food_id, amount, feeding_time)
values ((select id from animals where nickname = 'Симба'), (select id from food where name = 'Мясо говяжье'), 5.0,
        '12:00'),
       ((select id from animals where nickname = 'Дамбо'), (select id from food where name = 'Фрукты'), 10.0, '08:00'),
       ((select id from animals where nickname = 'Миша'), (select id from food where name = 'Комбикорм'), 7.0, '09:30'),
       ((select id from animals where nickname = 'Альфа'), (select id from food where name = 'Кролики'), 3.0, '14:00'),
       ((select id from animals where nickname = 'Жорик'), (select id from food where name = 'Овощи'), 8.0, '10:30'),
       ((select id from animals where nickname = 'Тигра'), (select id from food where name = 'Мясо говяжье'), 6.0,
        '13:00'),
       ((select id from animals where nickname = 'Балу'), (select id from food where name = 'Комбикорм'), 7.5, '08:30'),
       ((select id from animals where nickname = 'Гром'), (select id from food where name = 'Овощи'), 9.0, '10:00'),
       ((select id from animals where nickname = 'Тайсон'), (select id from food where name = 'Мясо говяжье'), 5.0,
        '12:30');

-- Поставщики
insert into suppliers (name, contacts)
values ('АгроФуд', '+7 495 555-12-34'),
       ('Зооферма', '+7 812 333-45-67'),
       ('Ферма Внуково', '+7 499 987-65-43'),
       ('ЭкоПродукт', '+7 903 456-78-90');

-- История поставок
insert into suppliers_food (supplier_id, food_id, delivery_date, quantity, price)
values ((select id from suppliers where name = 'АгроФуд'), (select id from food where name = 'Мясо говяжье'),
        '2024-02-15', 200.0, 50000),
       ((select id from suppliers where name = 'Зооферма'), (select id from food where name = 'Кролики'), '2024-02-20',
        50.0, 25000),
       ((select id from suppliers where name = 'Ферма Внуково'), (select id from food where name = 'Комбикорм'),
        '2024-03-05', 150.0, 30000);

-- Привязка сотрудников к животным
insert into employees_animal_types (employee_id, animal_type_id)
values ((select id from employees where full_name = 'Иван Петров'),
        (select id from animal_types where type = 'Лев')),
       ((select id from employees where full_name = 'Иван Петров'),
        (select id from animal_types where type = 'Волк')),
       ((select id from employees where full_name = 'Иван Петров'),
        (select id from animal_types where type = 'Медведь')),

       ((select id from employees where full_name = 'Артем Васильев'),
        (select id from animal_types where type = 'Лев')),
       ((select id from employees where full_name = 'Артем Васильев'),
        (select id from animal_types where type = 'Медведь')),

       ((select id from employees where full_name = 'Максим Соловьев'),
        (select id from animal_types where type = 'Лев')),
       ((select id from employees where full_name = 'Максим Соловьев'),
        (select id from animal_types where type = 'Волк')),

       ((select id from employees where full_name = 'Мария Сидорова'),
        (select id from animal_types where type = 'Жираф')),
       ((select id from employees where full_name = 'Ольга Иванова'),
        (select id from animal_types where type = 'Слон')),
       ((select id from employees where full_name = 'Дарья Орлова'),
        (select id from animal_types where type = 'Волк'));

-- Доступ сотрудников к клеткам
insert into employees_cages_access (employee_id, cage_id)
values
    -- уборщики
    ((select id from employees where full_name = 'Андрей Смирнов'), 1),
    ((select id from employees where full_name = 'Андрей Смирнов'), 2),
    ((select id from employees where full_name = 'Сергей Волков'), 3),
    ((select id from employees where full_name = 'Сергей Волков'), 4),
    ((select id from employees where full_name = 'Сергей Волков'), 5),

    --смотрители
    ((select id from employees where full_name = 'Мария Сидорова'), 5),
    ((select id from employees where full_name = 'Мария Сидорова'), 4),
    ((select id from employees where full_name = 'Ольга Иванова'), 3),
    ((select id from employees where full_name = 'Ольга Иванова'), 2),
    ((select id from employees where full_name = 'Дарья Орлова'), 1);

-- Мед записи
insert into medical_records (animal_id, age, weight, height, vaccinations, illnesses, checkup_date)
values ((select id from animals where nickname = 'Симба'), 3, 180.5, 1.10, 'Прививка A', null, '2023-06-01'),
       ((select id from animals where nickname = 'Дамбо'), 5, 1200, 2.50, null, 'Гастрит', '2023-02-15'),
       ((select id from animals where nickname = 'Миша'), 4, 200, 1.20, 'Прививка B', 'Паразиты', '2023-07-10');

-- История перемещений
insert into animal_cage_history (animal_id, cage_id, start_date)
values ((select id from animals where nickname = 'Симба'), 1, '2022-05-10'),
       ((select id from animals where nickname = 'Дамбо'), 2, '2021-09-15'),
       ((select id from animals where nickname = 'Миша'), 3, '2020-12-20'),
       ((select id from animals where nickname = 'Альфа'), 4, '2023-01-05'),
       ((select id from animals where nickname = 'Жорик'), 5, '2022-07-30'),
       ((select id from animals where nickname = 'Тигра'), 1, '2022-09-22'),
       ((select id from animals where nickname = 'Балу'), 3, '2020-05-19'),
       ((select id from animals where nickname = 'Луна'), 4, '2021-11-30'),
       ((select id from animals where nickname = 'Гром'), 5, '2023-03-17'),
       ((select id from animals where nickname = 'Тайсон'), 1, '2023-06-21');

-- Записи о рождении
insert into birth_records (parent_id_1, parent_id_2, birth_date, status)
values ((select id from animals where nickname = 'Симба'),
        (select id from animals where nickname = 'Альфа'),
        '2023-01-10',
        'оставлен');

-- Совместимость заполняется полностью автоматически

-- TODO: добавить эти данные в сущность suppliers_food
-- supplier_food
insert into supplier_food (supplier_id, food_id)
values ((select id from suppliers where name = 'АгроФуд'), (select id from food where name = 'Мясо говяжье')),
       ((select id from suppliers where name = 'Зооферма'), (select id from food where name = 'Мясо говяжье')),
       ((select id from suppliers where name = 'Зооферма'), (select id from food where name = 'Кролики')),
       ((select id from suppliers where name = 'Ферма Внуково'), (select id from food where name = 'Комбикорм')),
       ((select id from suppliers where name = 'ЭкоПродукт'), (select id from food where name = 'Овощи')),
       ((select id from suppliers where name = 'Ферма Внуково'), (select id from food where name = 'Овощи')),
       ((select id from suppliers where name = 'Зооферма'), (select id from food where name = 'Рыба')),
       ((select id from suppliers where name = 'Ферма Внуково'), (select id from food where name = 'Зерно')),
       ((select id from suppliers where name = 'ЭкоПродукт'), (select id from food where name = 'Фрукты'));

-- TODO: добавить больше данных
--  (для каждого животного должна быть минимум одна запись в medical_record, animal_cage_history и тд)