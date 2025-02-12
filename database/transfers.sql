-- перемещение
create table transfers
(
    id        serial primary key,
    animal_id int  not null,
    reason    text not null,
    destination int not null, -- куда перемещен
    transfer_date       date not null
);