create table if not exists old_names
(
    id          bigint not null
        primary key,
    first_name  varchar(20),
    middle_name varchar(55),
    last_name   varchar(55),
    birthday    date,
    member_id   bigint
        constraint member_old_names_link
            references family_members
);