alter table short_family_members add column if not exists info_id bigint;
alter table short_family_members add constraint short_family_member_info  FOREIGN KEY (info_id) references short_members_info(id);
