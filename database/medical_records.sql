create table medical_record
(
    id           serial primary key,
    animal_id    int not null,
    vaccinations jsonb,
    illnesses    jsonb,
    checkup_date      time
);