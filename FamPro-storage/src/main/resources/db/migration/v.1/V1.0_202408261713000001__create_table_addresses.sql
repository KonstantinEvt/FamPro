create table if not exists addresses
(
    id           bigint not null primary key,
    building     varchar(50),
    city         varchar(50),
    country      varchar(40),
    flat         varchar(20),
    intern_name  varchar(255),
    house        varchar(20),
    postcode     varchar(50),
    region       varchar(50),
    street       varchar(50),
    assignment   varchar(255)
        constraint addresses_assignment_check
            check ((assignment)::text = ANY
                   ((ARRAY ['WORK'::character varying, 'HOME'::character varying, 'MOBILE'::character varying])::text[])),
    description  varchar(255),
    secret_level varchar(255)
        constraint address_secret
            check ((secret_level)::text = ANY
                   ((ARRAY [
                       'OPEN'::character varying,
                       'CONFIDENTIAL'::character varying,
                       'FAMILY'::character varying,
                       'GENETIC_TREE'::character varying,
                       'BLOOD_TWO'::character varying,
                       'GLOBAL_TREE'::character varying,
                       'CLOSE'::character varying
                       ])::text[])),
    status       varchar(255)
        constraint addresses_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ON_LINK'::character varying, 'OUT_LINK'::character varying, 'BANNED'::character varying])::text[])),
    check_status varchar(255)
        constraint addresses_checkstatus_check
            check ((check_status)::text = ANY
                   ((ARRAY ['CHECKED'::character varying, 'UNCHECKED'::character varying])::text[])),
    owner_uuid   uuid,
    photo_exist boolean,
    tech_string  varchar(255)
);