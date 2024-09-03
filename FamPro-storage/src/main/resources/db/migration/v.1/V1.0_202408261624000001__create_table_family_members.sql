create table if not exists family_members
(
    id         bigint not null primary key,
    uuid       uuid unique,
    birthday   date,
    death_day  date,
    name       varchar(20),
    familiya   varchar(50),
    fathername varchar(50),
    sex        varchar(50)
        constraint family_members_sex_check
            check ((sex)::text = ANY ((ARRAY ['MALE'::character varying, 'FEMALE'::character varying])::text[])),
    father_id  bigint  CONSTRAINT family_member_farther REFERENCES family_members(id),
    mother_id  bigint  constraint family_member_mother  REFERENCES family_members (id)
            );