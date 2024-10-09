create table if not exists losing_parents
(
    id           bigint not null
        primary key,
    full_name    varchar(255),
    first_name   varchar(20),
    middle_name  varchar(55),
    last_name    varchar(55),
    birthday     date,
    uuid         uuid,
    losing_uuid  uuid,
    member_id    bigint
        constraint member_losing_parents_link
            references family_members,
    check_status varchar(50)
        constraint family_members_sex_check
            check ((check_status)::text = ANY
                   ((ARRAY ['ABSENT'::character varying, 'OTHER'::character varying])::text[]))
);