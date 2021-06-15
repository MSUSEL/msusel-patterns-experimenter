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

--==============================================================================
-- == ADDRESSBOOK - USER ==
--==============================================================================
-- change user first - we will be changing the table column names for the user
-- fields later, so we need to update to the new ids now
-- User - one to one relationship in hibernate shares the id code
update calendar_event set person_user=(select person from person_user pu where pu.id=calendar_event.person_user);
update calendar_event set modified_by=(select person from person_user pu where pu.id=calendar_event.modified_by);
update directory set person_user=(select person from person_user pu where pu.id=directory.person_user);
update file set person_user=(select person from person_user pu where pu.id=file.person_user);
update library_comment set person_user=(select person from person_user pu where pu.id=library_comment.person_user);
update library_item set person_user=(select person from person_user pu where pu.id=library_item.person_user);
update library_item set created_by=(select person from person_user pu where pu.id=library_item.created_by);
update library_item set modified_by=(select person from person_user pu where pu.id=library_item.modified_by);
update navigation_folder set person_user=(select person from person_user pu where pu.id=navigation_folder.person_user);
update navigation_menu_item set person_user=(select person from person_user pu where pu.id=navigation_menu_item.person_user);
update person set created_by=(select person from person_user pu where pu.id=person.created_by);
update person_group set created_by=(select person from person_user pu where pu.id=person_group.created_by);
update person_group_member set person_user=(select person from person_user pu where pu.id=person_group_member.person_user);
update setting set person_user=(select person from person_user pu where pu.id=setting.person_user);

update person_user set id=0-id;
update person_user set id=person;

-- compatibility with 7.2
-- alter table person_user drop column person;
create table person_user_tmp as select id, name, password, enable, deleted from person_user;
drop table person_user;
CREATE TABLE person_user (
    id integer DEFAULT nextval('person_user_id_seq'::text) NOT NULL,
    name character varying(25) NOT NULL,
    "password" character varying(25),
    enable boolean,
    deleted boolean
);
insert into person_user select * from person_user_tmp;
drop table person_user_tmp;
--==============================================================================

--==============================================================================
-- == ADMIN ==
--==============================================================================
-- Settings
create table setting_tmp as select * from setting;
-- enable is a primitive type - it cannot be null
update setting_tmp set enable=true where enable is null;
drop table setting;
CREATE TABLE setting (
    id integer DEFAULT nextval('setting_id_seq'::text) NOT NULL,
    name character varying(50),
    value character varying,
    "type" integer,
    person_user integer,
    description character varying,
    enable boolean not null,
    created timestamp not null,
    created_by integer not null,
    modified timestamp not null,
    modified_by integer not null
);
insert into setting select id, name, value, "type", person_user, description, enable, now(), 1, now(), 1 from setting_tmp;
drop table setting_tmp;
-- new setting values here --
insert into setting (name, value, type, description, enable, created, created_by, modified, modified_by) values ('emailAddressHost', 'locahost', 1, 'host used to append to email addresses generated from user names', true, now(), 1, now(), 1);
insert into setting (name, value, type, description, enable, created, created_by, modified, modified_by) values ('userNamePrefix', '', 1, 'Prepended to user names to get the actual system user name.', true, now(), 1, now(), 1);
insert into setting (name, value, type, description, enable, created, created_by, modified, modified_by) values ('siteDebug', 'true', 2, 'Set to true to display error messages on the screen. Otherwise, they are sent to the administrator.', true, now(), 1, now(), 1);
-- changed setting values here --
update setting set value='/usr/local/ivatagroupware/mailserver/sudo' where name='pathScriptMailServer';
update setting set value='ivata groupware' where name='siteName';
update setting set value='' where name='pathScriptMailServer';
-- change the name of the emailDebug setting to siteLoginDebug
update setting set name='siteLoginDebugPassword' where name='emailDebug';
update setting set value='false' where name='siteLoginDebugPassword';
-- change emailPersonalNamespace to emailFolderNamespace
update setting set name='emailFolderNamespace' where name='emailPersonalNamespace';
-- capitalize the HTML
update setting set name='emailHeaderReplyHTML' where name='emailHeaderReplyHtml';
update setting set name='emailHeaderForwardHTML' where name='emailHeaderForwardHtml';
--==============================================================================

--==============================================================================
-- === ADDRESS BOOK ===
--==============================================================================
-- Address - for now this is a one-to-one relationship with person; it makes
-- life a bit simpler and the front-end can't handle multiple addresses yet,
-- anyway
delete from address where person is null;
update address set id=0-id;
update address set id=person;
-- alter table address drop column person;
create table address_tmp as select id, street_address, town, post_code, address_country, region from address;
drop table address;
create table address (
    id integer DEFAULT nextval('address_id_seq'::text) NOT NULL,
    street_address character varying(100) NOT NULL,
    town character varying(50) NOT NULL,
    post_code character varying(15) NOT NULL,
    address_country integer,
    region character varying(50)
);
insert into address select * from address_tmp;
drop table address_tmp;

-- Group
create table perg_tmp as select * from person_group;
update perg_tmp set created_by=1 where created_by is null;
drop table person_group;
CREATE TABLE person_group (
    id integer DEFAULT nextval('person_group_id_seq'::text) NOT NULL,
    name character varying(100) NOT NULL,
    head integer,
    parent integer,
    description character varying(255),
    created timestamp not null,
    created_by integer not null,
    modified timestamp not null,
    modified_by integer not null
);
insert into person_group select id, name, head, parent, description, now(), created_by, now(), created_by from perg_tmp;
drop table perg_tmp;

-- Person
create table person_tmp as select * from person;
update person_tmp set created_by=1 where created_by is null;
drop table person;
CREATE TABLE person (
    id integer DEFAULT nextval('person_id_seq'::text) NOT NULL,
    first_names character varying(50) NOT NULL,
    last_name character varying(30) NOT NULL,
    date_of_birth date,
    company character varying(35),
    person_group integer,
    file_as character varying(35) NOT NULL,
    salutation character varying(6),
    job_title character varying(25),
    created timestamp not null,
    created_by integer not null,
    modified timestamp not null,
    modified_by integer not null,
    deleted boolean not null
);
insert into person select id, first_names, last_name, date_of_birth, company, person_group, file_as, salutation, job_title, now(), created_by, now(), created_by, false from person_tmp;
drop table person_tmp;
-- Telecom Address
create table ta_tmp as select * from telecom_address;
drop table telecom_address;
CREATE TABLE telecom_address (
    id integer DEFAULT nextval('telecom_address_id_seq'::text) NOT NULL,
    person integer,
    address character varying(100) NOT NULL,
    address_type integer NOT NULL,
    number integer NOT NULL,
    created timestamp not null,
    created_by integer not null,
    modified timestamp not null,
    modified_by integer not null
);
insert into telecom_address select id, person, address, address_type, number, now(), 1, now(), 1 from ta_tmp;
drop table ta_tmp;
--==============================================================================

--==============================================================================
-- === CALENDAR ===
--==============================================================================
-- Event
create table ce_tmp as select * from calendar_event;
update ce_tmp set created=now() where created is null;
update ce_tmp set modified_by=1 where modified_by is null;
drop table calendar_event;
CREATE TABLE calendar_event (
    id integer DEFAULT nextval('calendar_event_id_seq'::text) NOT NULL,
    "start" timestamp without time zone NOT NULL,
    finish timestamp without time zone,
    subject character varying(30) NOT NULL,
    description character varying,
    dayevent boolean,
    "type" integer,
    created timestamp not null,
    created_by integer not null,
    modified timestamp not null,
    modified_by integer not null
);
insert into calendar_event select id, "start", finish, subject, description, dayevent, "type", created, modified_by, created, modified_by from ce_tmp;
drop table ce_tmp;
--==============================================================================
-- Vacation
create table "vacation" (
  "calendar_event" integer,
  "approved" bool
);
--==============================================================================

--==============================================================================
-- === DRIVE ===
--==============================================================================
-- Directory
drop table directory;
drop table file;
--==============================================================================

--==============================================================================
-- === LIBRARY ===
--==============================================================================
-- Library Item
create table li_tmp as select * from library_item;
update li_tmp set created_by = person_user;
update li_tmp set created = now() where created is null;
update li_tmp set created_by = 1 where created_by is null;
update li_tmp set modified = created where modified is null;
update li_tmp set modified_by = created_by where modified_by is null;
drop table library_item;
CREATE TABLE library_item (
    id integer DEFAULT nextval('library_item_id_seq'::text) NOT NULL,
    "type" integer NOT NULL,
    library_topic integer,
    title character varying(100) NOT NULL,
    image_directory character varying(35),
    created timestamp not null,
    created_by integer not null,
    modified timestamp not null,
    modified_by integer not null,
    summary character varying
);
insert into library_item select id, "type", library_topic, title, image_directory, created, created_by, modified, modified_by, summary from li_tmp;
drop table li_tmp;
-- Library Item Comment
create table lc_tmp as select * from library_comment;
update lc_tmp set modified=now() where modified is null;
drop table library_comment;
CREATE TABLE library_comment (
    id integer DEFAULT nextval('library_comment_id_seq'::text) NOT NULL,
    id_reply_to integer,
    item integer,
    subject character varying(60) NOT NULL,
    text character varying NOT NULL,
    format integer,
    no_reply boolean,
    created timestamp not null,
    created_by integer not null,
    modified timestamp not null,
    modified_by integer not null
);
insert into library_comment select id, id_reply_to, item, subject, text, format, no_reply, modified, person_user, modified, person_user from lc_tmp;
drop table lc_tmp;
--==============================================================================

--==============================================================================
-- === NAVIGATION ===
--==============================================================================
update navigation_menu_item set text='post faqs' where text='post answers';
-- disable drive menu item for now
delete from navigation_menu_item where text='drive';
-- minutes are also out of this next release
delete from navigation_menu_item where text='write minutes';
delete from navigation_menu_item where text='plan meeting';
-- as are preferences
delete from navigation_menu_item where text='preferences';
-- and drafts folder on webmail
delete from navigation_menu_item where text='drafts';
--==============================================================================

--==============================================================================
-- === SEARCH ===
--==============================================================================
drop table search_index;
drop sequence "search_index_id_seq";
create sequence "search_index_id_seq" start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1;
create table "search_index" (
    "id" integer NOT NULL,
    "content" integer,
    "weight" float,
    "word" character varying(100) NOT NULL
);
create sequence "search_item_id_seq" start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1;
create table "search_item" (
    "id" integer NOT NULL,
    "category" character varying(100) NOT NULL,
    "target_id" integer,
    "type" character varying(100) NOT NULL
);
create sequence "search_item_content_id_seq" start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1;
create table "search_item_content" (
    "id" integer NOT NULL,
    "search_item" integer,
    "target_id" integer,
    "type" character varying(100) NOT NULL
);
--==============================================================================

