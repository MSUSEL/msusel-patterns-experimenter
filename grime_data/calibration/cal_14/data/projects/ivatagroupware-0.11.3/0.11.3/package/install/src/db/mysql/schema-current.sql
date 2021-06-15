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
drop table if exists search_stopword;
drop table if exists library_faq_category;
drop table if exists telecom_address;
drop table if exists calendar_event;
drop table if exists meeting_agenda_point;
drop table if exists person_user;
drop table if exists setting;
drop table if exists meeting;
drop table if exists public_holiday;
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
   id integer not null auto_increment,
   number integer,
   text varchar(255),
   library_item integer,
   search_item integer,
   primary key (id)
);
create table navigation_menu (
   id integer not null,
   priority integer,
   text varchar(255),
   primary key (id)
);
create table person (
   id integer not null auto_increment,
   modified datetime not null,
   company varchar(255),
   date_of_birth datetime,
   file_as varchar(255),
   first_names varchar(255),
   person_group integer,
   job_title varchar(255),
   last_name varchar(255),
   salutation varchar(255),
   deleted bit,
   created datetime,
   created_by integer,
   modified_by integer,
   primary key (id)
);
create table person_employee (
   id integer not null auto_increment,
   address_country integer,
   number varchar(255),
   region_code varchar(255),
   vacation_days integer,
   primary key (id)
);
create table library_item (
   id integer not null auto_increment,
   modified datetime not null,
   image_directory varchar(255),
   summary varchar(255),
   title varchar(255),
   library_topic integer,
   type integer,
   created datetime,
   created_by integer,
   modified_by integer,
   primary key (id)
);
create table address_country (
   id integer not null auto_increment,
   code varchar(255),
   name varchar(255),
   priority integer,
   primary key (id)
);
create table search_item (
   id integer not null auto_increment,
   category varchar(255),
   target_id integer,
   type varchar(255),
   primary key (id)
);
create table meeting_category (
   id integer not null auto_increment,
   meeting integer,
   name varchar(255),
   primary key (id)
);
create table person_group (
   id integer not null auto_increment,
   modified datetime not null,
   description varchar(255),
   head integer,
   name varchar(255),
   parent integer,
   created datetime,
   created_by integer,
   modified_by integer,
   primary key (id)
);
create table vacation (
   calendar_event integer not null,
   approved bit,
   primary key (calendar_event)
);
create table library_faq (
   id integer not null auto_increment,
   answer varchar(255),
   library_faq_category integer,
   question varchar(255),
   primary key (id)
);
create table search_item_content (
   id integer not null auto_increment,
   search_item integer,
   target_id integer,
   type varchar(255),
   primary key (id)
);
create table library_comment (
   id integer not null auto_increment,
   modified datetime not null,
   format integer,
   item integer,
   id_reply_to integer,
   subject varchar(255),
   text varchar(255),
   no_reply bit,
   created datetime,
   created_by integer,
   modified_by integer,
   primary key (id)
);
create table library_topic (
   id integer not null auto_increment,
   caption varchar(255),
   image varchar(255),
   primary key (id)
);
create table person_group_right (
   id integer not null auto_increment,
   access integer,
   detail integer,
   person_group integer,
   target_id integer,
   primary key (id)
);
create table person_group_member (
   person_group integer not null,
   person_user integer not null,
   primary key (person_user, person_group)
);
create table navigation_menu_item (
   id integer not null auto_increment,
   image varchar(255),
   menu integer,
   priority integer,
   text varchar(255),
   URL varchar(255),
   person_user integer,
   primary key (id)
);
create table person_group_right_detail (
   id integer not null auto_increment,
   description varchar(255),
   name varchar(255),
   primary key (id)
);
create table meeting_attendee (
   id integer not null auto_increment,
   confirmed bit,
   meeting integer,
   person integer,
   primary key (id)
);
create table search_index (
   id integer not null auto_increment,
   content integer,
   weight float,
   word varchar(255),
   primary key (id)
);
create table search_stopword (
   id integer not null auto_increment,
   word varchar(255),
   primary key (id)
);
create table library_faq_category (
   id integer not null auto_increment,
   description varchar(255),
   library_item integer,
   name varchar(255),
   primary key (id)
);
create table telecom_address (
   id integer not null auto_increment,
   modified datetime not null,
   address varchar(255),
   number integer,
   address_type integer,
   created datetime,
   created_by integer,
   modified_by integer,
   person integer,
   primary key (id)
);
create table calendar_event (
   id integer not null auto_increment,
   modified datetime not null,
   description varchar(255),
   finish datetime,
   start datetime,
   subject varchar(255),
   dayevent bit,
   created datetime,
   created_by integer,
   modified_by integer,
   primary key (id)
);
create table meeting_agenda_point (
   id integer not null auto_increment,
   category integer,
   minutesText varchar(255),
   name varchar(255),
   primary key (id)
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
   id integer not null auto_increment,
   modified datetime not null,
   description varchar(255),
   name varchar(255),
   type integer,
   person_user integer,
   value varchar(255),
   enable bit,
   created datetime,
   created_by integer,
   modified_by integer,
   primary key (id)
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
