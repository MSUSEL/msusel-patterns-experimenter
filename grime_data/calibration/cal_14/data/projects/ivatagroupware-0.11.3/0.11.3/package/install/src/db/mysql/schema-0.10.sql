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

SET FOREIGN_KEY_CHECKS = 0;
drop table if exists address;
drop table if exists library_page;
drop table if exists navigation_menu;
drop table if exists person;
drop table if exists person_employee;
drop table if exists library_item;
drop table if exists address_country;
drop table if exists search_item;
drop table if exists meeting_category;
drop table if exists person_group;
drop table if exists vacation;
drop table if exists library_faq;
drop table if exists search_item_content;
drop table if exists library_comment;
drop table if exists library_topic;
drop table if exists person_group_right;
drop table if exists person_group_member;
drop table if exists navigation_menu_item;
drop table if exists person_group_right_detail;
drop table if exists meeting_attendee;
drop table if exists search_index;
drop table if exists library_faq_category;
drop table if exists telecom_address;
drop table if exists calendar_event;
drop table if exists meeting_agenda_point;
drop table if exists person_user;
drop table if exists setting;
drop table if exists meeting;
drop table if exists public_holiday;

create table address (
   id INTEGER not null,
   address_country INTEGER,
   post_code VARCHAR(255),
   region VARCHAR(255),
   street_address VARCHAR(255),
   town VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1; 
create table library_page (
   id INTEGER NOT NULL AUTO_INCREMENT,
   number INTEGER,
   text VARCHAR(255),
   library_item INTEGER,
   search_item INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table navigation_menu (
   id INTEGER NOT NULL AUTO_INCREMENT,
   priority INTEGER,
   text VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table person (
   id INTEGER NOT NULL AUTO_INCREMENT,
   modified DATETIME not null,
   company VARCHAR(255),
   date_of_birth DATETIME,
   file_as VARCHAR(255),
   first_names VARCHAR(255),
   person_group INTEGER,
   job_title VARCHAR(255),
   last_name VARCHAR(255),
   salutation VARCHAR(255),
   deleted BIT,
   created DATETIME,
   created_by INTEGER,
   modified_by INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table person_employee (
   id INTEGER NOT NULL AUTO_INCREMENT,
   address_country INTEGER,
   number VARCHAR(255),
   region_code VARCHAR(255),
   vacation_days INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table library_item (
   id INTEGER NOT NULL AUTO_INCREMENT,
   modified DATETIME not null,
   image_directory VARCHAR(255),
   summary VARCHAR(255),
   title VARCHAR(255),
   library_topic INTEGER,
   type INTEGER,
   created DATETIME,
   created_by INTEGER,
   modified_by INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table address_country (
   id INTEGER NOT NULL AUTO_INCREMENT,
   code VARCHAR(255),
   name VARCHAR(255),
   priority INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table search_item (
   id INTEGER NOT NULL AUTO_INCREMENT,
   category VARCHAR(255),
   target_id INTEGER,
   type VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table meeting_category (
   id INTEGER NOT NULL AUTO_INCREMENT,
   meeting INTEGER,
   name VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table person_group (
   id INTEGER NOT NULL AUTO_INCREMENT,
   modified DATETIME not null,
   description VARCHAR(255),
   head INTEGER,
   name VARCHAR(255),
   parent INTEGER,
   created DATETIME,
   created_by INTEGER,
   modified_by INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table vacation (
   calendar_event INTEGER not null,
   approved BIT,
   primary key (calendar_event)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table library_faq (
   id INTEGER NOT NULL AUTO_INCREMENT,
   answer VARCHAR(255),
   library_faq_category INTEGER,
   question VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table search_item_content (
   id INTEGER NOT NULL AUTO_INCREMENT,
   search_item INTEGER,
   target_id INTEGER,
   type VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table library_comment (
   id INTEGER NOT NULL AUTO_INCREMENT,
   modified DATETIME not null,
   format INTEGER,
   item INTEGER,
   id_reply_to INTEGER,
   subject VARCHAR(255),
   text VARCHAR(255),
   no_reply BIT,
   created DATETIME,
   created_by INTEGER,
   modified_by INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table library_topic (
   id INTEGER NOT NULL AUTO_INCREMENT,
   caption VARCHAR(255),
   image VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table person_group_right (
   id INTEGER NOT NULL AUTO_INCREMENT,
   access INTEGER,
   detail INTEGER,
   person_group INTEGER,
   target_id INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table person_group_member (
   person_group INTEGER not null,
   person_user INTEGER not null,
   primary key (person_user, person_group)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table navigation_menu_item (
   id INTEGER NOT NULL AUTO_INCREMENT,
   image VARCHAR(255),
   menu INTEGER,
   priority INTEGER,
   text VARCHAR(255),
   URL VARCHAR(255),
   person_user INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table person_group_right_detail (
   id INTEGER NOT NULL AUTO_INCREMENT,
   description VARCHAR(255),
   name VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table meeting_attendee (
   id INTEGER NOT NULL AUTO_INCREMENT,
   confirmed BIT,
   meeting INTEGER,
   person INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table search_index (
   id INTEGER NOT NULL AUTO_INCREMENT,
   content INTEGER,
   weight FLOAT,
   word VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table library_faq_category (
   id INTEGER NOT NULL AUTO_INCREMENT,
   description VARCHAR(255),
   library_item INTEGER,
   name VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table telecom_address (
   id INTEGER NOT NULL AUTO_INCREMENT,
   modified DATETIME not null,
   address VARCHAR(255),
   number INTEGER,
   address_type INTEGER,
   created DATETIME,
   created_by INTEGER,
   modified_by INTEGER,
   person INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table calendar_event (
   id INTEGER NOT NULL AUTO_INCREMENT,
   modified DATETIME not null,
   description VARCHAR(255),
   finish DATETIME,
   start DATETIME,
   subject VARCHAR(255),
   dayevent BIT,
   created DATETIME,
   created_by INTEGER,
   modified_by INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table meeting_agenda_point (
   id INTEGER NOT NULL AUTO_INCREMENT,
   category INTEGER,
   minutesText VARCHAR(255),
   name VARCHAR(255),
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table person_user (
   id INTEGER not null,
   name VARCHAR(255),
   password VARCHAR(255),
   deleted BIT,
   enable BIT,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table setting (
   id INTEGER NOT NULL AUTO_INCREMENT,
   modified DATETIME not null,
   description VARCHAR(255),
   name VARCHAR(255),
   type INTEGER,
   person_user INTEGER,
   value VARCHAR(255),
   enable BIT,
   created DATETIME,
   created_by INTEGER,
   modified_by INTEGER,
   primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table meeting (
   calendar_event INTEGER not null,
   chair_person INTEGER,
   location VARCHAR(255),
   primary key (calendar_event)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
create table public_holiday (
   calendar_event INTEGER not null,
   country INTEGER,
   region_code VARCHAR(255),
   primary key (calendar_event)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
alter table address add index (address_country), add constraint FKBB979BF4BAAD664B foreign key (address_country) references address_country (id);
alter table library_page add index (search_item), add constraint FK9A17B393DFD7174A foreign key (search_item) references search_item (id);
alter table library_page add index (library_item), add constraint FK9A17B3939A14CC17 foreign key (library_item) references library_item (id);
alter table person add index (person_group), add constraint FKC4E39B55ECF26115 foreign key (person_group) references person_group (id);
alter table person add index (modified_by), add constraint FKC4E39B55A3F58D8D foreign key (modified_by) references person_user (id);
alter table person add index (created_by), add constraint FKC4E39B5551A3A90E foreign key (created_by) references person_user (id);
alter table person_employee add index (address_country), add constraint FKEAE56C58BAAD664B foreign key (address_country) references address_country (id);
alter table library_item add index (modified_by), add constraint FK9A14CC17A3F58D8D foreign key (modified_by) references person_user (id);
alter table library_item add index (created_by), add constraint FK9A14CC1751A3A90E foreign key (created_by) references person_user (id);
alter table library_item add index (library_topic), add constraint FK9A14CC17A91D9CAB foreign key (library_topic) references library_topic (id);
alter table meeting_category add index (meeting), add constraint FK22A1F68238264A3B foreign key (meeting) references meeting (calendar_event);
alter table person_group add index (parent), add constraint FKECF26115C4AB08AA foreign key (parent) references person_group (id);
alter table person_group add index (created_by), add constraint FKECF2611551A3A90E foreign key (created_by) references person_user (id);
alter table person_group add index (head), add constraint FKECF2611530CDE0 foreign key (head) references person (id);
alter table person_group add index (modified_by), add constraint FKECF26115A3F58D8D foreign key (modified_by) references person_user (id);
alter table vacation add index (calendar_event), add constraint FK9B8422DD2A9BEA59 foreign key (calendar_event) references calendar_event (id);
alter table library_faq add index (library_faq_category), add constraint FKDBAE09925756890B foreign key (library_faq_category) references library_faq_category (id);
alter table search_item_content add index (search_item), add constraint FKDFDF0884DFD7174A foreign key (search_item) references search_item (id);
alter table library_comment add index (id_reply_to), add constraint FK54B6CEDB6A473294 foreign key (id_reply_to) references library_comment (id);
alter table library_comment add index (modified_by), add constraint FK54B6CEDBA3F58D8D foreign key (modified_by) references person_user (id);
alter table library_comment add index (item), add constraint FK54B6CEDB317B13 foreign key (item) references library_item (id);
alter table library_comment add index (created_by), add constraint FK54B6CEDB51A3A90E foreign key (created_by) references person_user (id);
alter table person_group_right add index (detail), add constraint FK2B5CA272B06A1851 foreign key (detail) references person_group_right_detail (id);
alter table person_group_right add index (person_group), add constraint FK2B5CA272ECF26115 foreign key (person_group) references person_group (id);
alter table person_group_member add index (person_user), add constraint FK3779B504E6A2D675 foreign key (person_user) references person_user (id);
alter table person_group_member add index (person_group), add constraint FK3779B504ECF26115 foreign key (person_group) references person_group (id);
alter table navigation_menu_item add index (menu), add constraint FKBC9B1D8833155F foreign key (menu) references navigation_menu (id);
alter table navigation_menu_item add index (person_user), add constraint FKBC9B1D88E6A2D675 foreign key (person_user) references person_user (id);
alter table meeting_attendee add index (person), add constraint FK3FF9063EC4E39B55 foreign key (person) references person (id);
alter table meeting_attendee add index (meeting), add constraint FK3FF9063E38264A3B foreign key (meeting) references meeting (calendar_event);
alter table search_index add index (content), add constraint FK1B09137B38B73479 foreign key (content) references search_item_content (id);
alter table library_faq_category add index (library_item), add constraint FK5756890B9A14CC17 foreign key (library_item) references library_item (id);
alter table telecom_address add index (created_by), add constraint FK65DD614C51A3A90E foreign key (created_by) references person_user (id);
alter table telecom_address add index (person), add constraint FK65DD614CC4E39B55 foreign key (person) references person (id);
alter table telecom_address add index (modified_by), add constraint FK65DD614CA3F58D8D foreign key (modified_by) references person_user (id);
alter table calendar_event add index (created_by), add constraint FK2A9BEA5951A3A90E foreign key (created_by) references person_user (id);
alter table calendar_event add index (modified_by), add constraint FK2A9BEA59A3F58D8D foreign key (modified_by) references person_user (id);
alter table meeting_agenda_point add index (category), add constraint FK93007321302BCFE foreign key (category) references meeting_category (id);
alter table setting add index (modified_by), add constraint FK765F0E50A3F58D8D foreign key (modified_by) references person_user (id);
alter table setting add index (person_user), add constraint FK765F0E50E6A2D675 foreign key (person_user) references person_user (id);
alter table setting add index (created_by), add constraint FK765F0E5051A3A90E foreign key (created_by) references person_user (id);
alter table meeting add index (chair_person), add constraint FK38264A3B7F1A8A0F foreign key (chair_person) references person (id);
alter table meeting add index (calendar_event), add constraint FK38264A3B2A9BEA59 foreign key (calendar_event) references calendar_event (id);
alter table public_holiday add index (country), add constraint FKEBA2088239175796 foreign key (country) references address_country (id);
alter table public_holiday add index (calendar_event), add constraint FKEBA208822A9BEA59 foreign key (calendar_event) references calendar_event (id);
SET FOREIGN_KEY_CHECKS = 1;