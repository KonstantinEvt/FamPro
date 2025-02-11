create table if not exists family
(

    id           bigint not null
        primary key,
    birthday     date,
    death_day    date,
    description  varchar(255),
    uuid         uuid,
    string_id    varchar(255),
    husband_info varchar(255),
    wife_info    varchar(255),
    husband      bigint
        constraint husband_cons
            references short_family_members,
    wife         bigint
        constraint wife_cons
            references short_family_members
);