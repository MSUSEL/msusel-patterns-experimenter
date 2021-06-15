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
   id int not null,
   address_country int null,
   post_code varchar(255) null,
   region varchar(255) null,
   street_address varchar(255) null,
   town varchar(255) null,
   primary key (id)
);
create table library_page (
   id int not null,
   number int null,
   text varchar(255) null,
   library_item int null,
   search_item int null,
   primary key (id)
);
create table navigation_menu (
   id int not null,
   priority int null,
   text varchar(255) null,
   primary key (id)
);
create table person (
   id int not null,
   modified timestamp not null,
   company varchar(255) null,
   date_of_birth timestamp null,
   file_as varchar(255) null,
   first_names varchar(255) null,
   person_group int null,
   job_title varchar(255) null,
   last_name varchar(255) null,
   salutation varchar(255) null,
   deleted boolean null,
   created timestamp null,
   created_by int null,
   modified_by int null,
   primary key (id)
);
create table person_employee (
   id int not null,
   address_country int null,
   number varchar(255) null,
   region_code varchar(255) null,
   vacation_days int null,
   primary key (id)
);
create table library_item (
   id int not null,
   modified timestamp not null,
   image_directory varchar(255) null,
   summary varchar(255) null,
   title varchar(255) null,
   library_topic int null,
   type int null,
   created timestamp null,
   created_by int null,
   modified_by int null,
   primary key (id)
);
create table address_country (
   id int not null,
   code varchar(255) null,
   name varchar(255) null,
   priority int null,
   primary key (id)
);
create table search_item (
   id int not null,
   category varchar(255) null,
   target_id int null,
   type varchar(255) null,
   primary key (id)
);
create table meeting_category (
   id int not null,
   meeting int null,
   name varchar(255) null,
   primary key (id)
);
create table person_group (
   id int not null,
   modified timestamp not null,
   description varchar(255) null,
   head int null,
   name varchar(255) null,
   parent int null,
   created timestamp null,
   created_by int null,
   modified_by int null,
   primary key (id)
);
create table vacation (
   calendar_event int not null,
   approved boolean null,
   primary key (calendar_event)
);
create table library_faq (
   id int not null,
   answer varchar(255) null,
   library_faq_category int null,
   question varchar(255) null,
   primary key (id)
);
create table search_item_content (
   id int not null,
   search_item int null,
   target_id int null,
   type varchar(255) null,
   primary key (id)
);
create table library_comment (
   id int not null,
   modified timestamp not null,
   format int null,
   item int null,
   id_reply_to int null,
   subject varchar(255) null,
   text varchar(255) null,
   no_reply boolean null,
   created timestamp null,
   created_by int null,
   modified_by int null,
   primary key (id)
);
create table library_topic (
   id int not null,
   caption varchar(255) null,
   image varchar(255) null,
   primary key (id)
);
create table person_group_right (
   id int not null,
   access int null,
   detail int null,
   person_group int null,
   target_id int null,
   primary key (id)
);
create table person_group_member (
   person_group int not null,
   person_user int not null,
   primary key (person_user, person_group)
);
create table navigation_menu_item (
   id int not null,
   image varchar(255) null,
   menu int null,
   priority int null,
   text varchar(255) null,
   URL varchar(255) null,
   person_user int null,
   primary key (id)
);
create table person_group_right_detail (
   id int not null,
   description varchar(255) null,
   name varchar(255) null,
   primary key (id)
);
create table meeting_attendee (
   id int not null,
   confirmed boolean null,
   meeting int null,
   person int null,
   primary key (id)
);
create table search_index (
   id int not null,
   content int null,
   weight float null,
   word varchar(255) null,
   primary key (id)
);
create table search_stopword (
   id int not null,
   word varchar(255) null,
   primary key (id)
);
create table library_faq_category (
   id int not null,
   description varchar(255) null,
   library_item int null,
   name varchar(255) null,
   primary key (id)
);
create table telecom_address (
   id int not null,
   modified timestamp not null,
   address varchar(255) null,
   number int null,
   address_type int null,
   created timestamp null,
   created_by int null,
   modified_by int null,
   person int null,
   primary key (id)
);
create table calendar_event (
   id int not null,
   modified timestamp not null,
   description varchar(255) null,
   finish timestamp null,
   start timestamp null,
   subject varchar(255) null,
   dayevent boolean null,
   created timestamp null,
   created_by int null,
   modified_by int null,
   primary key (id)
);
create table meeting_agenda_point (
   id int not null,
   category int null,
   minutesText varchar(255) null,
   name varchar(255) null,
   primary key (id)
);
create table person_user (
   id int not null,
   name varchar(255) null,
   password varchar(255) null,
   deleted boolean null,
   enable boolean null,
   primary key (id)
);
create table setting (
   id int not null,
   modified timestamp not null,
   description varchar(255) null,
   name varchar(255) null,
   type int null,
   person_user int null,
   value varchar(255) null,
   enable boolean null,
   created timestamp null,
   created_by int null,
   modified_by int null,
   primary key (id)
);
create table meeting (
   calendar_event int not null,
   chair_person int null,
   location varchar(255) null,
   primary key (calendar_event)
);
create table public_holiday (
   calendar_event int not null,
   country int null,
   region_code varchar(255) null,
   primary key (calendar_event)
);
alter table address foreign key FKBB979BF4BAAD664B (address_country) references address_country;
alter table library_page foreign key FK9A17B393DFD7174A (search_item) references search_item;
alter table library_page foreign key FK9A17B3939A14CC17 (library_item) references library_item;
alter table person foreign key FKC4E39B55ECF26115 (person_group) references person_group;
alter table person foreign key FKC4E39B55A3F58D8D (modified_by) references person_user;
alter table person foreign key FKC4E39B5551A3A90E (created_by) references person_user;
alter table person_employee foreign key FKEAE56C58BAAD664B (address_country) references address_country;
alter table library_item foreign key FK9A14CC17A3F58D8D (modified_by) references person_user;
alter table library_item foreign key FK9A14CC1751A3A90E (created_by) references person_user;
alter table library_item foreign key FK9A14CC17A91D9CAB (library_topic) references library_topic;
alter table meeting_category foreign key FK22A1F68238264A3B (meeting) references meeting;
alter table person_group foreign key FKECF26115C4AB08AA (parent) references person_group;
alter table person_group foreign key FKECF2611551A3A90E (created_by) references person_user;
alter table person_group foreign key FKECF2611530CDE0 (head) references person;
alter table person_group foreign key FKECF26115A3F58D8D (modified_by) references person_user;
alter table vacation foreign key FK9B8422DD2A9BEA59 (calendar_event) references calendar_event;
alter table library_faq foreign key FKDBAE09925756890B (library_faq_category) references library_faq_category;
alter table search_item_content foreign key FKDFDF0884DFD7174A (search_item) references search_item;
alter table library_comment foreign key FK54B6CEDB6A473294 (id_reply_to) references library_comment;
alter table library_comment foreign key FK54B6CEDBA3F58D8D (modified_by) references person_user;
alter table library_comment foreign key FK54B6CEDB317B13 (item) references library_item;
alter table library_comment foreign key FK54B6CEDB51A3A90E (created_by) references person_user;
alter table person_group_right foreign key FK2B5CA272B06A1851 (detail) references person_group_right_detail;
alter table person_group_right foreign key FK2B5CA272ECF26115 (person_group) references person_group;
alter table person_group_member foreign key FK3779B504E6A2D675 (person_user) references person_user;
alter table person_group_member foreign key FK3779B504ECF26115 (person_group) references person_group;
alter table navigation_menu_item foreign key FKBC9B1D8833155F (menu) references navigation_menu;
alter table navigation_menu_item foreign key FKBC9B1D88E6A2D675 (person_user) references person_user;
alter table meeting_attendee foreign key FK3FF9063EC4E39B55 (person) references person;
alter table meeting_attendee foreign key FK3FF9063E38264A3B (meeting) references meeting;
alter table search_index foreign key FK1B09137B38B73479 (content) references search_item_content;
alter table library_faq_category foreign key FK5756890B9A14CC17 (library_item) references library_item;
alter table telecom_address foreign key FK65DD614C51A3A90E (created_by) references person_user;
alter table telecom_address foreign key FK65DD614CC4E39B55 (person) references person;
alter table telecom_address foreign key FK65DD614CA3F58D8D (modified_by) references person_user;
alter table calendar_event foreign key FK2A9BEA5951A3A90E (created_by) references person_user;
alter table calendar_event foreign key FK2A9BEA59A3F58D8D (modified_by) references person_user;
alter table meeting_agenda_point foreign key FK93007321302BCFE (category) references meeting_category;
alter table setting foreign key FK765F0E50A3F58D8D (modified_by) references person_user;
alter table setting foreign key FK765F0E50E6A2D675 (person_user) references person_user;
alter table setting foreign key FK765F0E5051A3A90E (created_by) references person_user;
alter table meeting foreign key FK38264A3B7F1A8A0F (chair_person) references person;
alter table meeting foreign key FK38264A3B2A9BEA59 (calendar_event) references calendar_event;
alter table public_holiday foreign key FKEBA2088239175796 (country) references address_country;
alter table public_holiday foreign key FKEBA208822A9BEA59 (calendar_event) references calendar_event;
create sequence hibernate_sequence;
