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

--
-- PostgreSQL database dump
--

SET client_encoding = 'SQL_ASCII';
SET SESSION AUTHORIZATION 'postgres';

--
-- TOC entry 5 (OID 19777)
-- Name: address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 39 (OID 19779)
-- Name: address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE address (
    id integer DEFAULT nextval('address_id_seq'::text) NOT NULL,
    person integer,
    street_address character varying(100) NOT NULL,
    town character varying(50) NOT NULL,
    post_code character varying(15) NOT NULL,
    address_country integer,
    region character varying(50)
);


--
-- TOC entry 6 (OID 19782)
-- Name: address_country_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_country_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 40 (OID 19784)
-- Name: address_country; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE address_country (
    id integer DEFAULT nextval('address_country_id_seq'::text) NOT NULL,
    code character(2) NOT NULL,
    name character varying(45) NOT NULL,
    priority integer
);


--
-- TOC entry 7 (OID 19787)
-- Name: calendar_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE calendar_event_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 41 (OID 19789)
-- Name: calendar_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE calendar_event (
    id integer DEFAULT nextval('calendar_event_id_seq'::text) NOT NULL,
    "start" timestamp without time zone NOT NULL,
    finish timestamp without time zone,
    subject character varying(30) NOT NULL,
    description character varying,
    dayevent boolean,
    modified timestamp without time zone NOT NULL,
    person_user integer,
    "type" integer,
    created timestamp without time zone,
    modified_by integer
);


--
-- TOC entry 8 (OID 19795)
-- Name: calendar_holiday_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE calendar_holiday_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 42 (OID 19797)
-- Name: calendar_holiday; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE calendar_holiday (
    id integer DEFAULT nextval('calendar_holiday_id_seq'::text) NOT NULL,
    date date NOT NULL,
    name character varying(30) NOT NULL,
    description character varying
);


--
-- TOC entry 9 (OID 19803)
-- Name: directory_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE directory_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 43 (OID 19805)
-- Name: directory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE directory (
    id integer DEFAULT nextval('directory_id_seq'::text) NOT NULL,
    person_user integer,
    name character varying(100) NOT NULL,
    parent integer,
    modified timestamp without time zone
);


--
-- TOC entry 10 (OID 19808)
-- Name: file_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE file_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 44 (OID 19810)
-- Name: file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE file (
    id integer DEFAULT nextval('file_id_seq'::text) NOT NULL,
    person_user integer,
    file_name character varying(100),
    mime_type character varying(30),
    head_revision character varying(20),
    modified timestamp without time zone,
    size integer,
    directory integer
);


--
-- TOC entry 11 (OID 19813)
-- Name: high_id_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE high_id_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 45 (OID 19815)
-- Name: high_id; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE high_id (
    id integer DEFAULT nextval('high_id_id_seq'::text) NOT NULL,
    name character varying(60) NOT NULL,
    high_id integer NOT NULL,
    modified timestamp without time zone
);


--
-- TOC entry 12 (OID 19818)
-- Name: library_comment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_comment_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 46 (OID 19820)
-- Name: library_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_comment (
    id integer DEFAULT nextval('library_comment_id_seq'::text) NOT NULL,
    id_reply_to integer,
    person_user integer,
    item integer,
    subject character varying(60) NOT NULL,
    text character varying NOT NULL,
    modified timestamp without time zone,
    format integer,
    no_reply boolean
);


--
-- TOC entry 13 (OID 19826)
-- Name: library_faq_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_faq_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 47 (OID 19828)
-- Name: library_faq; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_faq (
    id integer DEFAULT nextval('library_faq_id_seq'::text) NOT NULL,
    library_faq_category integer,
    question character varying(150) NOT NULL,
    answer character varying
);


--
-- TOC entry 14 (OID 19834)
-- Name: library_faq_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_faq_category_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 48 (OID 19836)
-- Name: library_faq_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_faq_category (
    id integer DEFAULT nextval('library_faq_category_id_seq'::text) NOT NULL,
    library_item integer,
    name character varying(60) NOT NULL,
    description character varying
);


--
-- TOC entry 15 (OID 19842)
-- Name: library_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_item_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 49 (OID 19844)
-- Name: library_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_item (
    id integer DEFAULT nextval('library_item_id_seq'::text) NOT NULL,
    "type" integer NOT NULL,
    person_user integer,
    library_topic integer,
    title character varying(100) NOT NULL,
    image_directory character varying(35),
    created_by integer,
    created timestamp without time zone NOT NULL,
    modified_by integer,
    modified timestamp without time zone,
    summary character varying
);


--
-- TOC entry 16 (OID 19850)
-- Name: library_page_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_page_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 50 (OID 19852)
-- Name: library_page; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_page (
    id integer DEFAULT nextval('library_page_id_seq'::text) NOT NULL,
    library_item integer,
    number integer NOT NULL,
    text character varying NOT NULL
);


--
-- TOC entry 17 (OID 19858)
-- Name: library_topic_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_topic_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 51 (OID 19860)
-- Name: library_topic; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_topic (
    id integer DEFAULT nextval('library_topic_id_seq'::text) NOT NULL,
    image character varying(125) NOT NULL,
    caption character varying
);


--
-- TOC entry 18 (OID 19866)
-- Name: mail_message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mail_message_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 52 (OID 19868)
-- Name: mail_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE mail_message (
    id integer DEFAULT nextval('mail_message_id_seq'::text) NOT NULL
);


--
-- TOC entry 19 (OID 19871)
-- Name: meeting_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE meeting_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 53 (OID 19873)
-- Name: meeting; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE meeting (
    id integer DEFAULT nextval('meeting_id_seq'::text) NOT NULL,
    calendar_event integer,
    library_item integer,
    "location" character varying(50),
    chair_person integer
);


--
-- TOC entry 20 (OID 19876)
-- Name: meeting_agenda_point_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE meeting_agenda_point_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 54 (OID 19878)
-- Name: meeting_agenda_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE meeting_agenda_point (
    id integer DEFAULT nextval('meeting_agenda_point_id_seq'::text) NOT NULL,
    category integer,
    name character varying NOT NULL,
    minutes_text character varying
);


--
-- TOC entry 21 (OID 19884)
-- Name: meeting_attendee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE meeting_attendee_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 55 (OID 19886)
-- Name: meeting_attendee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE meeting_attendee (
    id integer DEFAULT nextval('meeting_attendee_id_seq'::text) NOT NULL,
    person integer,
    meeting integer,
    confirmed boolean NOT NULL
);


--
-- TOC entry 22 (OID 19889)
-- Name: meeting_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE meeting_category_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 56 (OID 19891)
-- Name: meeting_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE meeting_category (
    id integer DEFAULT nextval('meeting_category_id_seq'::text) NOT NULL,
    meeting integer,
    name character varying(100) NOT NULL
);


--
-- TOC entry 23 (OID 19894)
-- Name: navigation_folder_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE navigation_folder_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 57 (OID 19896)
-- Name: navigation_folder; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE navigation_folder (
    id integer DEFAULT nextval('navigation_folder_id_seq'::text) NOT NULL,
    folder integer NOT NULL,
    open boolean NOT NULL,
    tree character varying(25) NOT NULL,
    person_user integer
);


--
-- TOC entry 24 (OID 19899)
-- Name: navigation_menu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE navigation_menu_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 58 (OID 19901)
-- Name: navigation_menu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE navigation_menu (
    id integer DEFAULT nextval('navigation_menu_id_seq'::text) NOT NULL,
    priority integer NOT NULL,
    text character varying(25) NOT NULL
);


--
-- TOC entry 25 (OID 19904)
-- Name: navigation_menu_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE navigation_menu_item_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 59 (OID 19906)
-- Name: navigation_menu_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE navigation_menu_item (
    id integer DEFAULT nextval('navigation_menu_item_id_seq'::text) NOT NULL,
    person_user integer,
    priority integer NOT NULL,
    menu integer,
    text character varying(100) NOT NULL,
    url character varying(100) NOT NULL,
    image character varying(50)
);


--
-- TOC entry 26 (OID 19909)
-- Name: person_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 60 (OID 19911)
-- Name: person; Type: TABLE; Schema: public; Owner: postgres
--

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
    created_by integer
);


--
-- TOC entry 27 (OID 19914)
-- Name: person_employee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_employee_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 61 (OID 19916)
-- Name: person_employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person_employee (
    id integer DEFAULT nextval('person_employee_id_seq'::text) NOT NULL,
    person integer,
    address_country integer,
    region_code character varying(10),
    vacation_days integer DEFAULT 0 NOT NULL,
    number character varying(25)
);


--
-- TOC entry 28 (OID 19920)
-- Name: person_group_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_group_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 62 (OID 19922)
-- Name: person_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person_group (
    id integer DEFAULT nextval('person_group_id_seq'::text) NOT NULL,
    name character varying(100) NOT NULL,
    head integer,
    parent integer,
    description character varying(255),
    created_by integer
);


--
-- TOC entry 29 (OID 19925)
-- Name: person_group_member_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_group_member_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 63 (OID 19927)
-- Name: person_group_member; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person_group_member (
    person_user integer NOT NULL,
    person_group integer NOT NULL
);


--
-- TOC entry 30 (OID 19929)
-- Name: person_group_right_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_group_right_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 64 (OID 19931)
-- Name: person_group_right; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person_group_right (
    id integer DEFAULT nextval('person_group_right_id_seq'::text) NOT NULL,
    target_id integer,
    person_group integer,
    detail integer,
    "access" integer NOT NULL
);


--
-- TOC entry 31 (OID 19934)
-- Name: person_group_right_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_group_right_detail_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 65 (OID 19936)
-- Name: person_group_right_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person_group_right_detail (
    id integer DEFAULT nextval('person_group_right_detail_id_seq'::text) NOT NULL,
    name character(40) NOT NULL,
    description character varying
);


--
-- TOC entry 32 (OID 19942)
-- Name: person_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_user_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 66 (OID 19944)
-- Name: person_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person_user (
    id integer DEFAULT nextval('person_user_id_seq'::text) NOT NULL,
    name character varying(25) NOT NULL,
    person integer,
    "password" character varying(25),
    enable boolean,
    deleted boolean
);


--
-- TOC entry 33 (OID 19947)
-- Name: public_holiday_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public_holiday_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 67 (OID 19949)
-- Name: public_holiday; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public_holiday (
    id integer DEFAULT nextval('public_holiday_id_seq'::text) NOT NULL,
    calendar_event integer,
    country integer,
    region_code character varying(10)
);


--
-- TOC entry 34 (OID 19952)
-- Name: search_index_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE search_index_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 68 (OID 19954)
-- Name: search_index; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE search_index (
    id integer DEFAULT nextval('search_index_id_seq'::text) NOT NULL,
    topic integer,
    "type" integer NOT NULL,
    target_id integer NOT NULL,
    item integer NOT NULL,
    word character varying(15) NOT NULL,
    weight double precision NOT NULL
);


--
-- TOC entry 35 (OID 19957)
-- Name: search_stopword_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE search_stopword_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 69 (OID 19959)
-- Name: search_stopword; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE search_stopword (
    id integer DEFAULT nextval('search_stopword_id_seq'::text) NOT NULL,
    word character varying(15) NOT NULL
);


--
-- TOC entry 36 (OID 19962)
-- Name: search_result_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE search_result_id_seq
    START 1
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 70 (OID 19964)
-- Name: search_result; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE search_result (
    id integer DEFAULT nextval('search_result_id_seq'::text) NOT NULL
);


--
-- TOC entry 37 (OID 19967)
-- Name: setting_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE setting_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 71 (OID 19969)
-- Name: setting; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE setting (
    id integer DEFAULT nextval('setting_id_seq'::text) NOT NULL,
    name character varying(50),
    value character varying,
    "type" integer,
    person_user integer,
    description character varying,
    enable boolean
);


--
-- TOC entry 38 (OID 19975)
-- Name: telecom_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE telecom_address_id_seq
    INCREMENT 1
    CACHE 1;


--
-- TOC entry 72 (OID 19977)
-- Name: telecom_address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE telecom_address (
    id integer DEFAULT nextval('telecom_address_id_seq'::text) NOT NULL,
    person integer,
    address character varying(100) NOT NULL,
    address_type integer NOT NULL,
    number integer NOT NULL
);


--
-- TOC entry 73 (OID 21004)
-- Name: address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- TOC entry 74 (OID 21006)
-- Name: address_country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address_country
    ADD CONSTRAINT address_country_pkey PRIMARY KEY (id);


--
-- TOC entry 75 (OID 21008)
-- Name: calendar_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY calendar_event
    ADD CONSTRAINT calendar_event_pkey PRIMARY KEY (id);


--
-- TOC entry 76 (OID 21010)
-- Name: calendar_holiday_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY calendar_holiday
    ADD CONSTRAINT calendar_holiday_pkey PRIMARY KEY (id);


--
-- TOC entry 77 (OID 21012)
-- Name: directory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY directory
    ADD CONSTRAINT directory_pkey PRIMARY KEY (id);


--
-- TOC entry 78 (OID 21014)
-- Name: file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY file
    ADD CONSTRAINT file_pkey PRIMARY KEY (id);


--
-- TOC entry 79 (OID 21016)
-- Name: high_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY high_id
    ADD CONSTRAINT high_id_pkey PRIMARY KEY (id);


--
-- TOC entry 80 (OID 21018)
-- Name: library_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_comment
    ADD CONSTRAINT library_comment_pkey PRIMARY KEY (id);


--
-- TOC entry 81 (OID 21020)
-- Name: library_faq_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_faq
    ADD CONSTRAINT library_faq_pkey PRIMARY KEY (id);


--
-- TOC entry 82 (OID 21022)
-- Name: library_faq_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_faq_category
    ADD CONSTRAINT library_faq_category_pkey PRIMARY KEY (id);


--
-- TOC entry 83 (OID 21024)
-- Name: library_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_item
    ADD CONSTRAINT library_item_pkey PRIMARY KEY (id);


--
-- TOC entry 84 (OID 21026)
-- Name: library_page_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_page
    ADD CONSTRAINT library_page_pkey PRIMARY KEY (id);


--
-- TOC entry 85 (OID 21028)
-- Name: library_topic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_topic
    ADD CONSTRAINT library_topic_pkey PRIMARY KEY (id);


--
-- TOC entry 86 (OID 21030)
-- Name: mail_message_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mail_message
    ADD CONSTRAINT mail_message_pkey PRIMARY KEY (id);


--
-- TOC entry 87 (OID 21032)
-- Name: meeting_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (id);


--
-- TOC entry 88 (OID 21034)
-- Name: meeting_agenda_point_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY meeting_agenda_point
    ADD CONSTRAINT meeting_agenda_point_pkey PRIMARY KEY (id);


--
-- TOC entry 89 (OID 21036)
-- Name: meeting_attendee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY meeting_attendee
    ADD CONSTRAINT meeting_attendee_pkey PRIMARY KEY (id);


--
-- TOC entry 90 (OID 21038)
-- Name: meeting_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY meeting_category
    ADD CONSTRAINT meeting_category_pkey PRIMARY KEY (id);


--
-- TOC entry 91 (OID 21040)
-- Name: navigation_folder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY navigation_folder
    ADD CONSTRAINT navigation_folder_pkey PRIMARY KEY (id);


--
-- TOC entry 92 (OID 21042)
-- Name: navigation_menu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY navigation_menu
    ADD CONSTRAINT navigation_menu_pkey PRIMARY KEY (id);


--
-- TOC entry 93 (OID 21044)
-- Name: navigation_menu_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY navigation_menu_item
    ADD CONSTRAINT navigation_menu_item_pkey PRIMARY KEY (id);


--
-- TOC entry 94 (OID 21046)
-- Name: person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 95 (OID 21048)
-- Name: person_employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_employee
    ADD CONSTRAINT person_employee_pkey PRIMARY KEY (id);


--
-- TOC entry 96 (OID 21050)
-- Name: person_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_group
    ADD CONSTRAINT person_group_pkey PRIMARY KEY (id);


--
-- TOC entry 97 (OID 21052)
-- Name: person_group_right_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_group_right
    ADD CONSTRAINT person_group_right_pkey PRIMARY KEY (id);


--
-- TOC entry 98 (OID 21054)
-- Name: person_group_right_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_group_right_detail
    ADD CONSTRAINT person_group_right_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 99 (OID 21056)
-- Name: person_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_user
    ADD CONSTRAINT person_user_pkey PRIMARY KEY (id);


--
-- TOC entry 100 (OID 21058)
-- Name: public_holiday_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public_holiday
    ADD CONSTRAINT public_holiday_pkey PRIMARY KEY (id);


--
-- TOC entry 101 (OID 21060)
-- Name: search_index_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY search_index
    ADD CONSTRAINT search_index_pkey PRIMARY KEY (id);


--
-- TOC entry 102 (OID 21062)
-- Name: search_stopword_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY search_stopword
    ADD CONSTRAINT search_stopword_pkey PRIMARY KEY (id);


--
-- TOC entry 103 (OID 21064)
-- Name: search_result_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY search_result
    ADD CONSTRAINT search_result_pkey PRIMARY KEY (id);


--
-- TOC entry 104 (OID 21066)
-- Name: setting_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY setting
    ADD CONSTRAINT setting_pkey PRIMARY KEY (id);


--
-- TOC entry 105 (OID 21068)
-- Name: telecom_address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY telecom_address
    ADD CONSTRAINT telecom_address_pkey PRIMARY KEY (id);


