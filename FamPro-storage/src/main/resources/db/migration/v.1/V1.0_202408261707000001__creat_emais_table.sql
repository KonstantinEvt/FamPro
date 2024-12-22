create table if not exists emails
(
    id           bigint not null primary key,
    intern_name  varchar(255),
    assignment   varchar(255)
        constraint emails_assignment_check
            check ((assignment)::text = ANY
                   ((ARRAY ['WORK'::character varying, 'HOME'::character varying, 'MOBILE'::character varying])::text[])),
    secret_level varchar(255)
        constraint emails_secret
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
        constraint emails_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ON_LINK'::character varying, 'OUT_LINK'::character varying, 'BANNED'::character varying])::text[])),
    check_status varchar(255)
        constraint emails_checkstatus_check
            check ((check_status)::text = ANY
                   ((ARRAY ['CHECKED'::character varying, 'UNCHECKED'::character varying])::text[])),

    owner_uuid   uuid,
    description  varchar(255),
    tech_string  varchar(255)
);