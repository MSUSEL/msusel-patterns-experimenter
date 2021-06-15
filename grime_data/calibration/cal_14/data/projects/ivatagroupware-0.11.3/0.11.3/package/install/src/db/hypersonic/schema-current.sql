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
drop table address if exists;
drop table library_page if exists;
drop table navigation_menu if exists;
drop table person if exists;
drop table person_employee if exists;
drop table library_item if exists;
drop table address_country if exists;
drop table search_item if exists;
drop table meeting_category if exists;
drop table person_group if exists;
drop table vacation if exists;
drop table library_faq if exists;
drop table search_item_content if exists;
drop table library_comment if exists;
drop table library_topic if exists;
drop table person_group_right if exists;
drop table person_group_member if exists;
drop table navigation_menu_item if exists;
drop table person_group_right_detail if exists;
drop table meeting_attendee if exists;
drop table search_index if exists;
drop table search_stopword if exists;
drop table library_faq_category if exists;
drop table telecom_address if exists;
drop table calendar_event if exists;
drop table meeting_agenda_point if exists;
drop table person_user if exists;
drop table setting if exists;
drop table meeting if exists;
drop table public_holiday if exists;
create table address (
   id integer not null,
   address_country integer,
   post_code varchar(255),
   region varchar(255),
   street_address varchar(255),
   town varchar(255),
   primary key (id)
);
create table library_page (
   id integer generated by default as identity (start with 1),
   number integer,
   text varchar(255),
   library_item integer,
   search_item integer
);
create table navigation_menu (
   id integer generated by default as identity (start with 1),
   priority integer,
   text varchar(255)
);
create table person (
   id integer generated by default as identity (start with 1),
   modified timestamp not null,
   company varchar(255),
   date_of_birth timestamp,
   file_as varchar(255),
   first_names varchar(255),
   person_group integer,
   job_title varchar(255),
   last_name varchar(255),
   salutation varchar(255),
   deleted bit,
   created timestamp,
   created_by integer,
   modified_by integer
);
create table person_employee (
   id integer generated by default as identity (start with 1),
   address_country integer,
   number varchar(255),
   region_code varchar(255),
   vacation_days integer
);
create table library_item (
   id integer generated by default as identity (start with 1),
   modified timestamp not null,
   image_directory varchar(255),
   summary varchar(255),
   title varchar(255),
   library_topic integer,
   type integer,
   created timestamp,
   created_by integer,
   modified_by integer
);
create table address_country (
   id integer generated by default as identity (start with 1),
   code varchar(255),
   name varchar(255),
   priority integer
);
create table search_item (
   id integer generated by default as identity (start with 1),
   category varchar(255),
   target_id integer,
   type varchar(255)
);
create table meeting_category (
   id integer generated by default as identity (start with 1),
   meeting integer,
   name varchar(255)
);
create table person_group (
   id integer generated by default as identity (start with 1),
   modified timestamp not null,
   description varchar(255),
   head integer,
   name varchar(255),
   parent integer,
   created timestamp,
   created_by integer,
   modified_by integer
);
create table vacation (
   calendar_event integer not null,
   approved bit,
   primary key (calendar_event)
);
create table library_faq (
   id integer generated by default as identity (start with 1),
   answer varchar(255),
   library_faq_category integer,
   question varchar(255)
);
create table search_item_content (
   id integer generated by default as identity (start with 1),
   search_item integer,
   target_id integer,
   type varchar(255)
);
create table library_comment (
   id integer generated by default as identity (start with 1),
   modified timestamp not null,
   format integer,
   item integer,
   id_reply_to integer,
   subject varchar(255),
   text varchar(255),
   no_reply bit,
   created timestamp,
   created_by integer,
   modified_by integer
);
create table library_topic (
   id integer generated by default as identity (start with 1),
   caption varchar(255),
   image varchar(255)
);
create table person_group_right (
   id integer generated by default as identity (start with 1),
   access integer,
   detail integer,
   person_group integer,
   target_id integer
);
create table person_group_member (
   person_group integer not null,
   person_user integer not null,
   primary key (person_user, person_group)
);
create table navigation_menu_item (
   id integer generated by default as identity (start with 1),
   image varchar(255),
   menu integer,
   priority integer,
   text varchar(255),
   URL varchar(255),
   person_user integer
);
create table person_group_right_detail (
   id integer generated by default as identity (start with 1),
   description varchar(255),
   name varchar(255)
);
create table meeting_attendee (
   id integer generated by default as identity (start with 1),
   confirmed bit,
   meeting integer,
   person integer
);
create table search_index (
   id integer generated by default as identity (start with 1),
   content integer,
   weight float,
   word varchar(255)
);
create table search_stopword (
   id integer generated by default as identity (start with 1),
   word varchar(255)
);
create table library_faq_category (
   id integer generated by default as identity (start with 1),
   description varchar(255),
   library_item integer,
   name varchar(255)
);
create table telecom_address (
   id integer generated by default as identity (start with 1),
   modified timestamp not null,
   address varchar(255),
   number integer,
   address_type integer,
   created timestamp,
   created_by integer,
   modified_by integer,
   person integer
);
create table calendar_event (
   id integer generated by default as identity (start with 1),
   modified timestamp not null,
   description varchar(255),
   finish timestamp,
   start timestamp,
   subject varchar(255),
   dayevent bit,
   created timestamp,
   created_by integer,
   modified_by integer
);
create table meeting_agenda_point (
   id integer generated by default as identity (start with 1),
   category integer,
   minutesText varchar(255),
   name varchar(255)
);
create table person_user (
   id integer not null,
   name varchar(255),
   password varchar(255),
   deleted bit,
   enable bit,
   primary key (id)
);
create table setting (
   id integer generated by default as identity (start with 1),
   modified timestamp not null,
   description varchar(255),
   name varchar(255),
   type integer,
   person_user integer,
   value varchar(255),
   enable bit,
   created timestamp,
   created_by integer,
   modified_by integer
);
create table meeting (
   calendar_event integer not null,
   chair_person integer,
   location varchar(255),
   primary key (calendar_event)
);
create table public_holiday (
   calendar_event integer not null,
   country integer,
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
