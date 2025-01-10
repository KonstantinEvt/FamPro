create table if not exists family_members
(
    id           bigint not null primary key,
    first_name   varchar(20),
    middle_name  varchar(55),
    last_name    varchar(55),
    creator      varchar(55),
    create_time  timestamp,
    prime_photo  boolean,
    father_id    bigint
        constraint family_member_father references family_members (id),
    father_info  varchar(255),
    mother_id    bigint
        constraint family_member_mother references family_members (id),
    mother_info  varchar(255),
    birthday     date,
    death_day    date,
    sex          varchar(50)
        constraint family_members_sex
            check ((sex)::text = ANY ((ARRAY ['MALE'::character varying, 'FEMALE'::character varying])::text[])),
    uuid         uuid unique,
    full_name    varchar(255),
    check_status varchar(50)
        constraint family_members_check
            check ((check_status)::text = ANY
                   ((ARRAY ['OTHER'::character varying, 'MODERATE'::character varying,'LINKED'::character varying,'CHECKED'::character varying,'UNCHECKED'::character varying])::text[]))
);
CREATE INDEX uuid_fm ON family_members (uuid);
