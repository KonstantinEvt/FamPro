create table if not exists family_children
(
    family_id bigint not null
        constraint family_children_link
            references family(id),
    member_id bigint not null
        constraint member_children_link
            references short_family_members(id),
    primary key (family_id, member_id)
);