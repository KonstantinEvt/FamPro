create table if not exists addresses
(
    id           bigint not null primary key,
    building     varchar(50),
    city         varchar(50),
    country      varchar(50),
    flat         varchar(50),
    full_address varchar(500),
    house        varchar(50),
    housing      varchar(50),
    postcode     varchar(50),
    region       varchar(50),
    street       varchar(50)
);