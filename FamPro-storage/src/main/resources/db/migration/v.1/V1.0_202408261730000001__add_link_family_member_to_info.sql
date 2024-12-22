alter table family_members add column if not exists info_id bigint;
alter table family_members add constraint family_member_info  FOREIGN KEY (info_id) references members_info(id);
alter table family_members add column if not exists burial_id bigint;
alter table family_members add constraint burial_address  FOREIGN KEY (burial_id) references burials(id);
alter table family_members add column if not exists birth_id bigint;
alter table family_members add constraint birth_address  FOREIGN KEY (birth_id) references births(id);