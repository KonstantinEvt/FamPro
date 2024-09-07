create table if not exists members_info
(
    id         bigint not null primary key,
    main_email varchar(50),
    phone_id   bigint
        CONSTRAINT family_member_phones REFERENCES phones (id),
    address_id bigint
        CONSTRAINT family_member_addresses REFERENCES addresses (id),
    uuid       uuid unique
);
