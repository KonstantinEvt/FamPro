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
                       'BLOOD_ONE'::character varying,
                       'BLOOD_TWO'::character varying,
                       'BLOOD_THREE'::character varying
                       ])::text[])),
    main_phone   varchar(50),
    secret_main_phone varchar(255)
        constraint short_main_phone_secret
            check ((secret_main_phone)::text = ANY
                   ((ARRAY [
                       'OPEN'::character varying,
                       'CONFIDENTIAL'::character varying,
                       'FAMILY'::character varying,
                       'BLOOD_ONE'::character varying,
                       'BLOOD_TWO'::character varying,
                       'BLOOD_THREE'::character varying
                       ])::text[])),
    main_address varchar(50),
    secret_main_address varchar(255)
        constraint short_main_address_secret
            check ((secret_main_address)::text = ANY
                   ((ARRAY [
                       'OPEN'::character varying,
                       'CONFIDENTIAL'::character varying,
                       'FAMILY'::character varying,
                       'BLOOD_ONE'::character varying,
                       'BLOOD_TWO'::character varying,
                       'BLOOD_THREE'::character varying
                       ])::text[])),
    uuid         uuid unique
);
