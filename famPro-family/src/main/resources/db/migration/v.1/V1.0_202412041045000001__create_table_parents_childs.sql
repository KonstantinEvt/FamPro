
create table if not exists parents_childs_shorts
(
    parent_id bigint not null
        constraint child_short_link
            references short_family_members,
    child_id bigint not null
        constraint member_child_short_link
            references short_family_members,
    primary key (parent_id, child_id)
);
