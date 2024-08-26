create table if not exists members_info
(
    id  bigint not null primary key,
    email_id bigint  CONSTRAINT family_member_emails REFERENCES emails(id),
    phone_id bigint  CONSTRAINT family_member_phones REFERENCES phones(id),
    address_id bigint  CONSTRAINT family_member_addresses REFERENCES addresses(id)
);
