
create table if not exists parents_childs
(
    parent_id bigint not null
        constraint child_link
            references short_family_members,
    child_id bigint not null
        constraint member_child_link
            references short_family_members,
    primary key (parent_id, child_id)
);
