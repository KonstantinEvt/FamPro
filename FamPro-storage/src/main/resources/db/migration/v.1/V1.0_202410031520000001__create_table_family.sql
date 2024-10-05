create table if not exists family
(
    id          bigint not null
        primary key,
    birthday    date,
    death_day   date,
    description varchar(255),
    uuid        uuid
);