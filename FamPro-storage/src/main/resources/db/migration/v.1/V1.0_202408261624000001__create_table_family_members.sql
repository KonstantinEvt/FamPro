create table if not exists family_members
(
    id          bigint not null primary key,
    name        varchar(20),
    fathername  varchar(50),
    familiya    varchar(50),
    father_id   bigint
        constraint family_member_farther references family_members (id),
    father_info varchar(255),
    mother_id   bigint
        constraint family_member_mother references family_members (id),
    mother_info varchar(255),
    birthday    date,
    death_day   date,
    sex         varchar(50)
        constraint family_members_sex_check
            check ((sex)::text = ANY ((ARRAY ['MALE'::character varying, 'FEMALE'::character varying])::text[])),
    uuid        uuid unique
);
CREATE INDEX uuid_fm ON family_members (uuid);
