create table if not exists emails
(
    id           bigint not null primary key,
    email_name   varchar(255),
    assignment   varchar(255)
        constraint emails_assignment_check
            check ((assignment)::text = ANY
                   ((ARRAY ['WORK'::character varying, 'HOME'::character varying, 'MOBILE'::character varying])::text[])),


    status       varchar(255)
        constraint emails_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ON_LINK'::character varying, 'OUT_LINK'::character varying, 'BANNED'::character varying])::text[])),
    check_status varchar(255)
        constraint emails_checkstatus_check
            check ((check_status)::text = ANY
                   ((ARRAY ['CHECKED'::character varying, 'UNCHECKED'::character varying])::text[])),

    uuid         uuid,
    description  varchar(255)
);