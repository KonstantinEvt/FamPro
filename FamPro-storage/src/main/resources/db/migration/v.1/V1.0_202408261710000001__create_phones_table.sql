create table if not exists phones
(
    id           bigint not null
        primary key,
    assignment   varchar(255)
        constraint phones_assignment_check
            check ((assignment)::text = ANY
                   ((ARRAY ['WORK'::character varying, 'HOME'::character varying, 'MOBILE'::character varying])::text[])),
    description  varchar(255),
    phone_number varchar(255),
    status       varchar(255)
        constraint phones_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ON_LINK'::character varying, 'OUT_LINK'::character varying, 'BANNED'::character varying])::text[]))
);