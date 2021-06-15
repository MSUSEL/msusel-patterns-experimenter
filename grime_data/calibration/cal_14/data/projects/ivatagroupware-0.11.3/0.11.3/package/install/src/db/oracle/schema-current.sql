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
drop table address cascade constraints;
drop table library_page cascade constraints;
drop table navigation_menu cascade constraints;
drop table person cascade constraints;
drop table person_employee cascade constraints;
drop table library_item cascade constraints;
drop table address_country cascade constraints;
drop table search_item cascade constraints;
drop table meeting_category cascade constraints;
drop table person_group cascade constraints;
drop table vacation cascade constraints;
drop table library_faq cascade constraints;
drop table search_item_content cascade constraints;
drop table library_comment cascade constraints;
drop table library_topic cascade constraints;
drop table person_group_right cascade constraints;
drop table person_group_member cascade constraints;
drop table navigation_menu_item cascade constraints;
drop table person_group_right_detail cascade constraints;
drop table meeting_attendee cascade constraints;
drop table search_index cascade constraints;
drop table search_stopword cascade constraints;
drop table library_faq_category cascade constraints;
drop table telecom_address cascade constraints;
drop table calendar_event cascade constraints;
drop table meeting_agenda_point cascade constraints;
drop table person_user cascade constraints;
drop table setting cascade constraints;
drop table meeting cascade constraints;
drop table public_holiday cascade constraints;
drop sequence hibernate_sequence;
create table address (
   id number(10,0) not null,
   address_country number(10,0),
   post_code varchar2(255),
   region varchar2(255),
   street_address varchar2(255),
   town varchar2(255),
   primary key (id)
);
create table library_page (
   id number(10,0) not null,
   number number(10,0),
   text varchar2(255),
   library_item number(10,0),
   search_item number(10,0),
   primary key (id)
);
create table navigation_menu (
   id number(10,0) not null,
   priority number(10,0),
   text varchar2(255),
   primary key (id)
);
create table person (
   id number(10,0) not null,
   modified date not null,
   company varchar2(255),
   date_of_birth date,
   file_as varchar2(255),
   first_names varchar2(255),
   person_group number(10,0),
   job_title varchar2(255),
   last_name varchar2(255),
   salutation varchar2(255),
   deleted number(1,0),
   created date,
   created_by number(10,0),
   modified_by number(10,0),
   primary key (id)
);
create table person_employee (
   id number(10,0) not null,
   address_country number(10,0),
   number varchar2(255),
   region_code varchar2(255),
   vacation_days number(10,0),
   primary key (id)
);
create table library_item (
   id number(10,0) not null,
   modified date not null,
   image_directory varchar2(255),
   summary varchar2(255),
   title varchar2(255),
   library_topic number(10,0),
   type number(10,0),
   created date,
   created_by number(10,0),
   modified_by number(10,0),
   primary key (id)
);
create table address_country (
   id number(10,0) not null,
   code varchar2(255),
   name varchar2(255),
   priority number(10,0),
   primary key (id)
);
create table search_item (
   id number(10,0) not null,
   category varchar2(255),
   target_id number(10,0),
   type varchar2(255),
   primary key (id)
);
create table meeting_category (
   id number(10,0) not null,
   meeting number(10,0),
   name varchar2(255),
   primary key (id)
);
create table person_group (
   id number(10,0) not null,
   modified date not null,
   description varchar2(255),
   head number(10,0),
   name varchar2(255),
   parent number(10,0),
   created date,
   created_by number(10,0),
   modified_by number(10,0),
   primary key (id)
);
create table vacation (
   calendar_event number(10,0) not null,
   approved number(1,0),
   primary key (calendar_event)
);
create table library_faq (
   id number(10,0) not null,
   answer varchar2(255),
   library_faq_category number(10,0),
   question varchar2(255),
   primary key (id)
);
create table search_item_content (
   id number(10,0) not null,
   search_item number(10,0),
   target_id number(10,0),
   type varchar2(255),
   primary key (id)
);
create table library_comment (
   id number(10,0) not null,
   modified date not null,
   format number(10,0),
   item number(10,0),
   id_reply_to number(10,0),
   subject varchar2(255),
   text varchar2(255),
   no_reply number(1,0),
   created date,
   created_by number(10,0),
   modified_by number(10,0),
   primary key (id)
);
create table library_topic (
   id number(10,0) not null,
   caption varchar2(255),
   image varchar2(255),
   primary key (id)
);
create table person_group_right (
   id number(10,0) not null,
   access number(10,0),
   detail number(10,0),
   person_group number(10,0),
   target_id number(10,0),
   primary key (id)
);
create table person_group_member (
   person_group number(10,0) not null,
   person_user number(10,0) not null,
   primary key (person_user, person_group)
);
create table navigation_menu_item (
   id number(10,0) not null,
   image varchar2(255),
   menu number(10,0),
   priority number(10,0),
   text varchar2(255),
   URL varchar2(255),
   person_user number(10,0),
   primary key (id)
);
create table person_group_right_detail (
   id number(10,0) not null,
   description varchar2(255),
   name varchar2(255),
   primary key (id)
);
create table meeting_attendee (
   id number(10,0) not null,
   confirmed number(1,0),
   meeting number(10,0),
   person number(10,0),
   primary key (id)
);
create table search_index (
   id number(10,0) not null,
   content number(10,0),
   weight float,
   word varchar2(255),
   primary key (id)
);
create table search_stopword (
   id number(10,0) not null,
   word varchar2(255),
   primary key (id)
);
create table library_faq_category (
   id number(10,0) not null,
   description varchar2(255),
   library_item number(10,0),
   name varchar2(255),
   primary key (id)
);
create table telecom_address (
   id number(10,0) not null,
   modified date not null,
   address varchar2(255),
   number number(10,0),
   address_type number(10,0),
   created date,
   created_by number(10,0),
   modified_by number(10,0),
   person number(10,0),
   primary key (id)
);
create table calendar_event (
   id number(10,0) not null,
   modified date not null,
   description varchar2(255),
   finish date,
   start date,
   subject varchar2(255),
   dayevent number(1,0),
   created date,
   created_by number(10,0),
   modified_by number(10,0),
   primary key (id)
);
create table meeting_agenda_point (
   id number(10,0) not null,
   category number(10,0),
   minutesText varchar2(255),
   name varchar2(255),
   primary key (id)
);
create table person_user (
   id number(10,0) not null,
   name varchar2(255),
   password varchar2(255),
   deleted number(1,0),
   enable number(1,0),
   primary key (id)
);
create table setting (
   id number(10,0) not null,
   modified date not null,
   description varchar2(255),
   name varchar2(255),
   type number(10,0),
   person_user number(10,0),
   value varchar2(255),
   enable number(1,0),
   created date,
   created_by number(10,0),
   modified_by number(10,0),
   primary key (id)
);
create table meeting (
   calendar_event number(10,0) not null,
   chair_person number(10,0),
   location varchar2(255),
   primary key (calendar_event)
);
create table public_holiday (
   calendar_event number(10,0) not null,
   country number(10,0),
   region_code varchar2(255),
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
