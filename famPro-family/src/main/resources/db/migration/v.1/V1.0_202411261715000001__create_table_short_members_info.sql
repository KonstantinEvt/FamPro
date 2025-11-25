create table if not exists short_members_info
(
    id           bigint not null primary key,
    main_email   varchar(50),
    secret_main_email varchar(255)
        constraint short_main_email_secret
            check ((secret_main_email)::text = ANY
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
    main_phone   varchar(50),
    secret_main_phone varchar(255)
        constraint short_main_phone_secret
            check ((secret_main_phone)::text = ANY
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
    main_address varchar(50),
    secret_main_address varchar(255)
        constraint short_main_address_secret
            check ((secret_main_address)::text = ANY
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
    secret_biometric varchar(255)
        constraint biometric_secret
            check ((secret_biometric)::text = ANY
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
    uuid         uuid
);
