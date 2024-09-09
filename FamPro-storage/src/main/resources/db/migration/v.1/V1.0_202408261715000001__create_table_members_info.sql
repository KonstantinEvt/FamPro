create table if not exists members_info
(
    id           bigint not null primary key,
    main_email   varchar(50),
    main_phone   varchar(50),
    main_address varchar(50),
    uuid         uuid unique
);
