create table if not exists emails
(
    id          bigint not null primary key,
    assignment  varchar(255)
        constraint emails_assignment_check
            check ((assignment)::text = ANY
                   ((ARRAY ['WORK'::character varying, 'HOME'::character varying, 'MOBILE'::character varying])::text[])),
    description varchar(255),
    emailName       varchar(255),
    status      varchar(255)
        constraint emails_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ON_LINK'::character varying, 'OUT_LINK'::character varying, 'BANNED'::character varying])::text[]))
);