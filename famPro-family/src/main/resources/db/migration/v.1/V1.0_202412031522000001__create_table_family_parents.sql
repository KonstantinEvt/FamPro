create table if not exists family_parents
(
    family_id bigint not null
        constraint family_parents_link
            references family(id),
    member_id bigint not null
        constraint member_parent_link
            references short_family_members(id),
    primary key (family_id, member_id)
);