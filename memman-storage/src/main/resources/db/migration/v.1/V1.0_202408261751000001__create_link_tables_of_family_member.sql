create table if not exists addresses_of_family_member
(
    member_info_id bigint not null
        constraint member_info_address
            references members_info (id),
    address_id     bigint not null
        constraint address_of_family_member references addresses (id),
        primary key (member_info_id,  address_id)
);
create table if not exists emails_of_family_member
(
    member_info_id bigint not null
        constraint member_info_email
            references members_info (id),
    email_id       bigint not null
        constraint email_of_family_member references emails (id),
    primary key (member_info_id,  email_id)

);
create table if not exists phones_of_family_member
(
    member_info_id bigint not null
        constraint member_info_phone
            references members_info (id),
    phone_id       bigint not null
        constraint phone_of_family_member references phones (id),
    primary key (member_info_id,  phone_id)
);