create table user_online
(
    id                bigint not null
        primary key,
    email             varchar(255),
    extern_uuid       varchar(255),
    last_entering     timestamp(6),
    link_extern_id    varchar(255),
    localisation      varchar(255)
        constraint user_online_localisation_check
            check ((localisation)::text = ANY ((ARRAY ['EN'::character varying, 'RU'::character varying])::text[])),
    nick              varchar(255),
    exist_prime_photo boolean,
    priority_role     varchar(255)
        constraint user_online_priority_role_check
            check ((priority_role)::text = ANY
                   ((ARRAY ['ADMIN'::character varying, 'MANAGER'::character varying, 'VIP'::character varying, 'CHECKED'::character varying, 'LINKED_USER'::character varying, 'BASE_USER'::character varying, 'SIMPLE_USER'::character varying])::text[]))
);

alter table user_online
    owner to postgres;

