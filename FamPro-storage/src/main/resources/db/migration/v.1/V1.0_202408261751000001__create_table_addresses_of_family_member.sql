create table if not exists addresses_of_family_member
(
    member_info_id bigint not null
        constraint member_info_address
            references members_info(id),
    addresses_id    bigint not null
        constraint address_of_family_member  references addresses(id)
);
create table if not exists emails_of_family_member
(
    member_info_id bigint not null
        constraint member_info_email
            references members_info(id),
    addresses_id    bigint not null
        constraint email_of_family_member  references emails(id)
);
create table if not exists phones_of_family_member
(
    member_info_id bigint not null
        constraint member_info_phone
            references members_info(id),
    addresses_id    bigint not null
        constraint phone_of_family_member  references phones(id)
);