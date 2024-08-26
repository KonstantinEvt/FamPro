alter table family_members add column if not exists info_id bigint;
alter table family_members add constraint family_member_info  FOREIGN KEY (info_id) references members_info(id);