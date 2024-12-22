create table if not exists burials
(
    id           bigint not null primary key,
    grave        varchar(50),
    city         varchar(50),
    country      varchar(40),
    square       varchar(100),
    intern_name  varchar(255),
    chapter      varchar(100),
    cemetery     varchar(255),
    region       varchar(50),
    street       varchar(50),
    assignment   varchar(255)
        constraint burials_assignment_check
            check ((assignment)::text = ANY
                   ((ARRAY ['WORK'::character varying, 'HOME'::character varying, 'MOBILE'::character varying])::text[])),
    description  varchar(255),
    secret_level varchar(255)
        constraint burials_secret
            check ((secret_level)::text = ANY
                   ((ARRAY [
                       'OPEN'::character varying,
                       'CONFIDENTIAL'::character varying,
                       'FAMILY'::character varying,
                       'BLOOD_ONE'::character varying,
                       'BLOOD_TWO'::character varying,
                       'BLOOD_THREE'::character varying
                       ])::text[])),
    status       varchar(255)
        constraint burials_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ON_LINK'::character varying, 'OUT_LINK'::character varying, 'BANNED'::character varying])::text[])),
    check_status varchar(255)
        constraint burials_checkstatus_check
            check ((check_status)::text = ANY
                   ((ARRAY ['CHECKED'::character varying, 'UNCHECKED'::character varying])::text[])),
    owner_uuid   uuid,
    photo_exist boolean,
    tech_string  varchar(255)
);