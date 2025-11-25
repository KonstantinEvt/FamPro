create table if not exists phones
(
    id           bigint not null primary key,
    intern_name  varchar(255),
    assignment   varchar(255)
        constraint phones_assignment_check
            check ((assignment)::text = ANY
                   ((ARRAY ['WORK'::character varying, 'HOME'::character varying, 'MOBILE'::character varying])::text[])),
    description  varchar(255),
    secret_level varchar(255)
        constraint phone_secret
            check ((secret_level)::text = ANY
                   ((ARRAY [
                       'OPEN'::character varying,
                       'CONFIDENTIAL'::character varying,
                       'FAMILY'::character varying,
                       'GENETIC_TREE'::character varying,
                       'ANCESTOR'::character varying,
                       'STRAIGHT_BLOOD'::character varying,
                       'CLOSE'::character varying,
                       'UNDEFINED'::character varying
                       ])::text[])),
    status       varchar(255)
        constraint phones_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ON_LINK'::character varying, 'OUT_LINK'::character varying, 'BANNED'::character varying])::text[])),
    check_status varchar(255)
        constraint phones_checkstatus_check
            check ((check_status)::text = ANY
                   ((ARRAY ['CHECKED'::character varying, 'UNCHECKED'::character varying])::text[])),
    owner_uuid   uuid,
    tech_string  varchar(255)
);