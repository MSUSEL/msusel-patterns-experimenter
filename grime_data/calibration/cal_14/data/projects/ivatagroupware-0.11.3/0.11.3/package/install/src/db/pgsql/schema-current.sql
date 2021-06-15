--
-- The MIT License (MIT)
--
-- MSUSEL Arc Framework
-- Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
-- Software Engineering Laboratory and Idaho State University, Informatics and
-- Computer Science, Empirical Software Engineering Laboratory
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in all
-- copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
-- SOFTWARE.
--

alter table address drop constraint FKBB979BF4BAAD664B;
alter table library_page drop constraint FK9A17B393DFD7174A;
alter table library_page drop constraint FK9A17B3939A14CC17;
alter table person drop constraint FKC4E39B55ECF26115;
alter table person drop constraint FKC4E39B55A3F58D8D;
alter table person drop constraint FKC4E39B5551A3A90E;
alter table person_employee drop constraint FKEAE56C58BAAD664B;
alter table library_item drop constraint FK9A14CC17A3F58D8D;
alter table library_item drop constraint FK9A14CC1751A3A90E;
alter table library_item drop constraint FK9A14CC17A91D9CAB;
alter table meeting_category drop constraint FK22A1F68238264A3B;
alter table person_group drop constraint FKECF26115C4AB08AA;
alter table person_group drop constraint FKECF2611551A3A90E;
alter table person_group drop constraint FKECF2611530CDE0;
alter table person_group drop constraint FKECF26115A3F58D8D;
alter table vacation drop constraint FK9B8422DD2A9BEA59;
alter table library_faq drop constraint FKDBAE09925756890B;
alter table search_item_content drop constraint FKDFDF0884DFD7174A;
alter table library_comment drop constraint FK54B6CEDB6A473294;
alter table library_comment drop constraint FK54B6CEDBA3F58D8D;
alter table library_comment drop constraint FK54B6CEDB317B13;
alter table library_comment drop constraint FK54B6CEDB51A3A90E;
alter table person_group_right drop constraint FK2B5CA272B06A1851;
alter table person_group_right drop constraint FK2B5CA272ECF26115;
alter table person_group_member drop constraint FK3779B504E6A2D675;
alter table person_group_member drop constraint FK3779B504ECF26115;
alter table navigation_menu_item drop constraint FKBC9B1D8833155F;
alter table navigation_menu_item drop constraint FKBC9B1D88E6A2D675;
alter table meeting_attendee drop constraint FK3FF9063EC4E39B55;
alter table meeting_attendee drop constraint FK3FF9063E38264A3B;
alter table search_index drop constraint FK1B09137B38B73479;
alter table library_faq_category drop constraint FK5756890B9A14CC17;
alter table telecom_address drop constraint FK65DD614C51A3A90E;
alter table telecom_address drop constraint FK65DD614CC4E39B55;
alter table telecom_address drop constraint FK65DD614CA3F58D8D;
alter table calendar_event drop constraint FK2A9BEA5951A3A90E;
alter table calendar_event drop constraint FK2A9BEA59A3F58D8D;
alter table meeting_agenda_point drop constraint FK93007321302BCFE;
alter table setting drop constraint FK765F0E50A3F58D8D;
alter table setting drop constraint FK765F0E50E6A2D675;
alter table setting drop constraint FK765F0E5051A3A90E;
alter table meeting drop constraint FK38264A3B7F1A8A0F;
alter table meeting drop constraint FK38264A3B2A9BEA59;
alter table public_holiday drop constraint FKEBA2088239175796;
alter table public_holiday drop constraint FKEBA208822A9BEA59;
drop table address;
drop table library_page;
drop table navigation_menu;
drop table person;
drop table person_employee;
drop table library_item;
drop table address_country;
drop table search_item;
drop table meeting_category;
drop table person_group;
drop table vacation;
drop table library_faq;
drop table search_item_content;
drop table library_comment;
drop table library_topic;
drop table person_group_right;
drop table person_group_member;
drop table navigation_menu_item;
drop table person_group_right_detail;
drop table meeting_attendee;
drop table search_index;
drop table search_stopword;
drop table library_faq_category;
drop table telecom_address;
drop table calendar_event;
drop table meeting_agenda_point;
drop table person_user;
drop table setting;
drop table meeting;
drop table public_holiday;
drop sequence hibernate_sequence;
create table address (
   id int4 not null,
   address_country int4,
   post_code varchar(255),
   region varchar(255),
   street_address varchar(255),
   town varchar(255),
   primary key (id)
);
create table library_page (
   id int4 not null,
   number int4,
   text varchar(255),
   library_item int4,
   search_item int4,
   primary key (id)
);
create table navigation_menu (
   id int4 not null,
   priority int4,
   text varchar(255),
   primary key (id)
);
create table person (
   id int4 not null,
   modified timestamp not null,
   company varchar(255),
   date_of_birth timestamp,
   file_as varchar(255),
   first_names varchar(255),
   person_group int4,
   job_title varchar(255),
   last_name varchar(255),
   salutation varchar(255),
   deleted bool,
   created timestamp,
   created_by int4,
   modified_by int4,
   primary key (id)
);
create table person_employee (
   id int4 not null,
   address_country int4,
   number varchar(255),
   region_code varchar(255),
   vacation_days int4,
   primary key (id)
);
create table library_item (
   id int4 not null,
   modified timestamp not null,
   image_directory varchar(255),
   summary varchar(255),
   title varchar(255),
   library_topic int4,
   type int4,
   created timestamp,
   created_by int4,
   modified_by int4,
   primary key (id)
);
create table address_country (
   id int4 not null,
   code varchar(255),
   name varchar(255),
   priority int4,
   primary key (id)
);
create table search_item (
   id int4 not null,
   category varchar(255),
   target_id int4,
   type varchar(255),
   primary key (id)
);
create table meeting_category (
   id int4 not null,
   meeting int4,
   name varchar(255),
   primary key (id)
);
create table person_group (
   id int4 not null,
   modified timestamp not null,
   description varchar(255),
   head int4,
   name varchar(255),
   parent int4,
   created timestamp,
   created_by int4,
   modified_by int4,
   primary key (id)
);
create table vacation (
   calendar_event int4 not null,
   approved bool,
   primary key (calendar_event)
);
create table library_faq (
   id int4 not null,
   answer varchar(255),
   library_faq_category int4,
   question varchar(255),
   primary key (id)
);
create table search_item_content (
   id int4 not null,
   search_item int4,
   target_id int4,
   type varchar(255),
   primary key (id)
);
create table library_comment (
   id int4 not null,
   modified timestamp not null,
   format int4,
   item int4,
   id_reply_to int4,
   subject varchar(255),
   text varchar(255),
   no_reply bool,
   created timestamp,
   created_by int4,
   modified_by int4,
   primary key (id)
);
create table library_topic (
   id int4 not null,
   caption varchar(255),
   image varchar(255),
   primary key (id)
);
create table person_group_right (
   id int4 not null,
   access int4,
   detail int4,
   person_group int4,
   target_id int4,
   primary key (id)
);
create table person_group_member (
   person_group int4 not null,
   person_user int4 not null,
   primary key (person_user, person_group)
);
create table navigation_menu_item (
   id int4 not null,
   image varchar(255),
   menu int4,
   priority int4,
   text varchar(255),
   URL varchar(255),
   person_user int4,
   primary key (id)
);
create table person_group_right_detail (
   id int4 not null,
   description varchar(255),
   name varchar(255),
   primary key (id)
);
create table meeting_attendee (
   id int4 not null,
   confirmed bool,
   meeting int4,
   person int4,
   primary key (id)
);
create table search_index (
   id int4 not null,
   content int4,
   weight float4,
   word varchar(255),
   primary key (id)
);
create table search_stopword (
   id int4 not null,
   word varchar(255),
   primary key (id)
);
create table library_faq_category (
   id int4 not null,
   description varchar(255),
   library_item int4,
   name varchar(255),
   primary key (id)
);
create table telecom_address (
   id int4 not null,
   modified timestamp not null,
   address varchar(255),
   number int4,
   address_type int4,
   created timestamp,
   created_by int4,
   modified_by int4,
   person int4,
   primary key (id)
);
create table calendar_event (
   id int4 not null,
   modified timestamp not null,
   description varchar(255),
   finish timestamp,
   start timestamp,
   subject varchar(255),
   dayevent bool,
   created timestamp,
   created_by int4,
   modified_by int4,
   primary key (id)
);
create table meeting_agenda_point (
   id int4 not null,
   category int4,
   minutesText varchar(255),
   name varchar(255),
   primary key (id)
);
create table person_user (
   id int4 not null,
   name varchar(255),
   password varchar(255),
   deleted bool,
   enable bool,
   primary key (id)
);
create table setting (
   id int4 not null,
   modified timestamp not null,
   description varchar(255),
   name varchar(255),
   type int4,
   person_user int4,
   value varchar(255),
   enable bool,
   created timestamp,
   created_by int4,
   modified_by int4,
   primary key (id)
);
create table meeting (
   calendar_event int4 not null,
   chair_person int4,
   location varchar(255),
   primary key (calendar_event)
);
create table public_holiday (
   calendar_event int4 not null,
   country int4,
   region_code varchar(255),
   primary key (calendar_event)
);
alter table address add constraint FKBB979BF4BAAD664B foreign key (address_country) references address_country;
alter table library_page add constraint FK9A17B393DFD7174A foreign key (search_item) references search_item;
alter table library_page add constraint FK9A17B3939A14CC17 foreign key (library_item) references library_item;
alter table person add constraint FKC4E39B55ECF26115 foreign key (person_group) references person_group;
alter table person add constraint FKC4E39B55A3F58D8D foreign key (modified_by) references person_user;
alter table person add constraint FKC4E39B5551A3A90E foreign key (created_by) references person_user;
alter table person_employee add constraint FKEAE56C58BAAD664B foreign key (address_country) references address_country;
alter table library_item add constraint FK9A14CC17A3F58D8D foreign key (modified_by) references person_user;
alter table library_item add constraint FK9A14CC1751A3A90E foreign key (created_by) references person_user;
alter table library_item add constraint FK9A14CC17A91D9CAB foreign key (library_topic) references library_topic;
alter table meeting_category add constraint FK22A1F68238264A3B foreign key (meeting) references meeting;
alter table person_group add constraint FKECF26115C4AB08AA foreign key (parent) references person_group;
alter table person_group add constraint FKECF2611551A3A90E foreign key (created_by) references person_user;
alter table person_group add constraint FKECF2611530CDE0 foreign key (head) references person;
alter table person_group add constraint FKECF26115A3F58D8D foreign key (modified_by) references person_user;
alter table vacation add constraint FK9B8422DD2A9BEA59 foreign key (calendar_event) references calendar_event;
alter table library_faq add constraint FKDBAE09925756890B foreign key (library_faq_category) references library_faq_category;
alter table search_item_content add constraint FKDFDF0884DFD7174A foreign key (search_item) references search_item;
alter table library_comment add constraint FK54B6CEDB6A473294 foreign key (id_reply_to) references library_comment;
alter table library_comment add constraint FK54B6CEDBA3F58D8D foreign key (modified_by) references person_user;
alter table library_comment add constraint FK54B6CEDB317B13 foreign key (item) references library_item;
alter table library_comment add constraint FK54B6CEDB51A3A90E foreign key (created_by) references person_user;
alter table person_group_right add constraint FK2B5CA272B06A1851 foreign key (detail) references person_group_right_detail;
alter table person_group_right add constraint FK2B5CA272ECF26115 foreign key (person_group) references person_group;
alter table person_group_member add constraint FK3779B504E6A2D675 foreign key (person_user) references person_user;
alter table person_group_member add constraint FK3779B504ECF26115 foreign key (person_group) references person_group;
alter table navigation_menu_item add constraint FKBC9B1D8833155F foreign key (menu) references navigation_menu;
alter table navigation_menu_item add constraint FKBC9B1D88E6A2D675 foreign key (person_user) references person_user;
alter table meeting_attendee add constraint FK3FF9063EC4E39B55 foreign key (person) references person;
alter table meeting_attendee add constraint FK3FF9063E38264A3B foreign key (meeting) references meeting;
alter table search_index add constraint FK1B09137B38B73479 foreign key (content) references search_item_content;
alter table library_faq_category add constraint FK5756890B9A14CC17 foreign key (library_item) references library_item;
alter table telecom_address add constraint FK65DD614C51A3A90E foreign key (created_by) references person_user;
alter table telecom_address add constraint FK65DD614CC4E39B55 foreign key (person) references person;
alter table telecom_address add constraint FK65DD614CA3F58D8D foreign key (modified_by) references person_user;
alter table calendar_event add constraint FK2A9BEA5951A3A90E foreign key (created_by) references person_user;
alter table calendar_event add constraint FK2A9BEA59A3F58D8D foreign key (modified_by) references person_user;
alter table meeting_agenda_point add constraint FK93007321302BCFE foreign key (category) references meeting_category;
alter table setting add constraint FK765F0E50A3F58D8D foreign key (modified_by) references person_user;
alter table setting add constraint FK765F0E50E6A2D675 foreign key (person_user) references person_user;
alter table setting add constraint FK765F0E5051A3A90E foreign key (created_by) references person_user;
alter table meeting add constraint FK38264A3B7F1A8A0F foreign key (chair_person) references person;
alter table meeting add constraint FK38264A3B2A9BEA59 foreign key (calendar_event) references calendar_event;
alter table public_holiday add constraint FKEBA2088239175796 foreign key (country) references address_country;
alter table public_holiday add constraint FKEBA208822A9BEA59 foreign key (calendar_event) references calendar_event;
create sequence hibernate_sequence;
