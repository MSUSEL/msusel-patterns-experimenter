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

\connect - postgres

SET search_path = public, pg_catalog;

--
-- TOC entry 2 (OID 135220)
-- Name: address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 70 (OID 135222)
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
-- TOC entry 4 (OID 135227)
-- Name: address_country_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_country_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 71 (OID 135229)
-- Name: address_country; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE address_country (
    id integer DEFAULT nextval('address_country_id_seq'::text) NOT NULL,
    code character(2) NOT NULL,
    name character varying(45) NOT NULL,
    priority integer
);


--
-- TOC entry 6 (OID 135234)
-- Name: calendar_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE calendar_event_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 72 (OID 135236)
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
-- TOC entry 8 (OID 135244)
-- Name: calendar_holiday_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE calendar_holiday_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 73 (OID 135246)
-- Name: calendar_holiday; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE calendar_holiday (
    id integer DEFAULT nextval('calendar_holiday_id_seq'::text) NOT NULL,
    date date NOT NULL,
    name character varying(30) NOT NULL,
    description character varying
);


--
-- TOC entry 10 (OID 135254)
-- Name: directory_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE directory_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 74 (OID 135256)
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
-- TOC entry 12 (OID 135261)
-- Name: file_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE file_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 75 (OID 135263)
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
-- TOC entry 14 (OID 135268)
-- Name: high_id_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE high_id_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 76 (OID 135270)
-- Name: high_id; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE high_id (
    id integer DEFAULT nextval('high_id_id_seq'::text) NOT NULL,
    name character varying(60) NOT NULL,
    high_id integer NOT NULL,
    modified timestamp without time zone
);


--
-- TOC entry 16 (OID 135275)
-- Name: library_comment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_comment_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 77 (OID 135277)
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
-- TOC entry 18 (OID 135285)
-- Name: library_faq_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_faq_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 78 (OID 135287)
-- Name: library_faq; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_faq (
    id integer DEFAULT nextval('library_faq_id_seq'::text) NOT NULL,
    library_faq_category integer,
    question character varying(150) NOT NULL,
    answer character varying
);


--
-- TOC entry 20 (OID 135295)
-- Name: library_faq_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_faq_category_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 79 (OID 135297)
-- Name: library_faq_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_faq_category (
    id integer DEFAULT nextval('library_faq_category_id_seq'::text) NOT NULL,
    library_item integer,
    name character varying(60) NOT NULL,
    description character varying
);


--
-- TOC entry 22 (OID 135305)
-- Name: library_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_item_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 80 (OID 135307)
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
-- TOC entry 24 (OID 135315)
-- Name: library_page_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_page_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 81 (OID 135317)
-- Name: library_page; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_page (
    id integer DEFAULT nextval('library_page_id_seq'::text) NOT NULL,
    library_item integer,
    number integer NOT NULL,
    text character varying NOT NULL
);


--
-- TOC entry 26 (OID 135325)
-- Name: library_topic_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE library_topic_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 82 (OID 135327)
-- Name: library_topic; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE library_topic (
    id integer DEFAULT nextval('library_topic_id_seq'::text) NOT NULL,
    image character varying(125) NOT NULL,
    caption character varying
);


--
-- TOC entry 28 (OID 135335)
-- Name: mail_message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mail_message_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 83 (OID 135337)
-- Name: mail_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE mail_message (
    id integer DEFAULT nextval('mail_message_id_seq'::text) NOT NULL
);


--
-- TOC entry 30 (OID 135342)
-- Name: meeting_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE meeting_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 84 (OID 135344)
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
-- TOC entry 32 (OID 135349)
-- Name: meeting_agenda_point_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE meeting_agenda_point_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 85 (OID 135351)
-- Name: meeting_agenda_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE meeting_agenda_point (
    id integer DEFAULT nextval('meeting_agenda_point_id_seq'::text) NOT NULL,
    category integer,
    name character varying NOT NULL,
    minutes_text character varying
);


--
-- TOC entry 34 (OID 135359)
-- Name: meeting_attendee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE meeting_attendee_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 86 (OID 135361)
-- Name: meeting_attendee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE meeting_attendee (
    id integer DEFAULT nextval('meeting_attendee_id_seq'::text) NOT NULL,
    person integer,
    meeting integer,
    confirmed boolean NOT NULL
);


--
-- TOC entry 36 (OID 135366)
-- Name: meeting_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE meeting_category_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 87 (OID 135368)
-- Name: meeting_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE meeting_category (
    id integer DEFAULT nextval('meeting_category_id_seq'::text) NOT NULL,
    meeting integer,
    name character varying(100) NOT NULL
);


--
-- TOC entry 38 (OID 135373)
-- Name: navigation_folder_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE navigation_folder_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 88 (OID 135375)
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
-- TOC entry 40 (OID 135380)
-- Name: navigation_menu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE navigation_menu_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 89 (OID 135382)
-- Name: navigation_menu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE navigation_menu (
    id integer DEFAULT nextval('navigation_menu_id_seq'::text) NOT NULL,
    priority integer NOT NULL,
    text character varying(25) NOT NULL
);


--
-- TOC entry 42 (OID 135387)
-- Name: navigation_menu_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE navigation_menu_item_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 90 (OID 135389)
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
-- TOC entry 44 (OID 135394)
-- Name: person_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 91 (OID 135396)
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
-- TOC entry 46 (OID 135401)
-- Name: person_employee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_employee_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 92 (OID 135403)
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
-- TOC entry 48 (OID 135409)
-- Name: person_group_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_group_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 93 (OID 135411)
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
-- TOC entry 50 (OID 135416)
-- Name: person_group_member_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_group_member_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 94 (OID 135418)
-- Name: person_group_member; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person_group_member (
    person_user integer NOT NULL,
    person_group integer NOT NULL
);


--
-- TOC entry 52 (OID 135420)
-- Name: person_group_right_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_group_right_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 95 (OID 135422)
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
-- TOC entry 54 (OID 135427)
-- Name: person_group_right_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_group_right_detail_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 96 (OID 135429)
-- Name: person_group_right_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person_group_right_detail (
    id integer DEFAULT nextval('person_group_right_detail_id_seq'::text) NOT NULL,
    name character(40) NOT NULL,
    description character varying
);


--
-- TOC entry 56 (OID 135437)
-- Name: person_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_user_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 97 (OID 135439)
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
-- TOC entry 58 (OID 135444)
-- Name: public_holiday_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public_holiday_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 98 (OID 135446)
-- Name: public_holiday; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public_holiday (
    id integer DEFAULT nextval('public_holiday_id_seq'::text) NOT NULL,
    calendar_event integer,
    country integer,
    region_code character varying(10)
);


--
-- TOC entry 60 (OID 135451)
-- Name: search_index_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE search_index_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 99 (OID 135453)
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
-- TOC entry 62 (OID 135458)
-- Name: search_stopword_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE search_stopword_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 100 (OID 135460)
-- Name: search_stopword; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE search_stopword (
    id integer DEFAULT nextval('search_stopword_id_seq'::text) NOT NULL,
    word character varying(15) NOT NULL
);


--
-- TOC entry 64 (OID 135465)
-- Name: search_result_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE search_result_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 101 (OID 135467)
-- Name: search_result; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE search_result (
    id integer DEFAULT nextval('search_result_id_seq'::text) NOT NULL
);


--
-- TOC entry 66 (OID 135472)
-- Name: setting_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE setting_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 102 (OID 135474)
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
-- TOC entry 68 (OID 135482)
-- Name: telecom_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE telecom_address_id_seq
    START 1
    INCREMENT 1
    MAXVALUE 9223372036854775807
    MINVALUE 1
    CACHE 1;


--
-- TOC entry 103 (OID 135484)
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
-- Data for TOC entry 137 (OID 135222)
-- Name: address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY address (id, person, street_address, town, post_code, address_country, region) FROM stdin;
\.


--
-- Data for TOC entry 138 (OID 135229)
-- Name: address_country; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY address_country (id, code, name, priority) FROM stdin;
1	AD	country.AD	\N
2	AE	country.AE	\N
3	AF	country.AF	\N
4	AG	country.AG	\N
15	AI	country.AI	\N
16	AL	country.AL	\N
17	AM	country.AM	\N
18	AN	country.AN	\N
19	AO	country.AO	\N
20	AQ	country.AQ	\N
21	AR	country.AR	\N
22	AS	country.AS	\N
23	AT	country.AT	\N
24	AU	country.AU	\N
25	AV	country.AV	\N
26	AW	country.AW	\N
27	AZ	country.AZ	\N
28	BA	country.BA	\N
29	BB	country.BB	\N
30	BD	country.BD	\N
31	BE	country.BE	\N
32	BF	country.BF	\N
33	BG	country.BG	\N
34	BH	country.BH	\N
35	BI	country.BI	\N
36	BJ	country.BJ	\N
37	BM	country.BM	\N
38	BN	country.BN	\N
39	BO	country.BO	\N
40	BR	country.BR	\N
41	BS	country.BS	\N
42	BT	country.BT	\N
43	BV	country.BV	\N
44	BW	country.BW	\N
45	BY	country.BY	\N
46	BZ	country.BZ	\N
47	CA	country.CA	\N
48	CC	country.CC	\N
49	CD	country.CD	\N
50	CF	country.CF	\N
51	CG	country.CG	\N
52	CH	country.CH	\N
53	CK	country.CK	\N
54	CL	country.CL	\N
55	CM	country.CM	\N
56	CN	country.CN	\N
57	CO	country.CO	\N
58	CR	country.CR	\N
59	CU	country.CU	\N
60	CV	country.CV	\N
61	CX	country.CX	\N
62	CY	country.CY	\N
63	CZ	country.CZ	\N
64	DE	country.DE	\N
65	DJ	country.DJ	\N
66	DK	country.DK	\N
67	DM	country.DM	\N
68	DO	country.DO	\N
69	DZ	country.DZ	\N
70	EC	country.EC	\N
71	AA	country.AA	\N
72	EE	country.EE	\N
73	EG	country.EG	\N
74	EH	country.EH	\N
75	ER	country.ER	\N
76	ES	country.ES	\N
77	ET	country.ET	\N
78	FI	country.FI	\N
79	FJ	country.FJ	\N
80	FK	country.FK	\N
81	FM	country.FM	\N
82	FO	country.FO	\N
83	FR	country.FR	\N
84	FX	country.FX	\N
85	GA	country.GA	\N
86	GB	country.GB	\N
87	GD	country.GD	\N
88	GE	country.GE	\N
89	GF	country.GF	\N
90	GH	country.GH	\N
91	GI	country.GI	\N
92	GL	country.GL	\N
93	GM	country.GM	\N
94	GN	country.GN	\N
95	GP	country.GP	\N
96	GQ	country.GQ	\N
97	GR	country.GR	\N
98	GT	country.GT	\N
99	GU	country.GU	\N
100	GW	country.GW	\N
101	GY	country.GY	\N
102	HK	country.HK	\N
103	HM	country.HM	\N
104	HN	country.HN	\N
105	HR	country.HR	\N
106	HT	country.HT	\N
107	HU	country.HU	\N
108	ID	country.ID	\N
109	IE	country.IE	\N
110	IL	country.IL	\N
111	IN	country.IN	\N
112	IO	country.IO	\N
113	IQ	country.IQ	\N
114	IR	country.IR	\N
115	IS	country.IS	\N
116	IT	country.IT	\N
117	JM	country.JM	\N
118	JO	country.JO	\N
119	JP	country.JP	\N
120	KE	country.KE	\N
121	KG	country.KG	\N
122	KH	country.KH	\N
123	KI	country.KI	\N
124	KM	country.KM	\N
125	KN	country.KN	\N
126	KP	country.KP	\N
127	KR	country.KR	\N
128	KW	country.KW	\N
129	KY	country.KY	\N
130	KZ	country.KZ	\N
131	LA	country.LA	\N
132	LB	country.LB	\N
133	LC	country.LC	\N
134	LI	country.LI	\N
135	LK	country.LK	\N
136	LR	country.LR	\N
137	LS	country.LS	\N
138	LT	country.LT	\N
139	LU	country.LU	\N
140	LV	country.LV	\N
141	LY	country.LY	\N
142	MA	country.MA	\N
143	MC	country.MC	\N
144	MD	country.MD	\N
145	MG	country.MG	\N
146	MH	country.MH	\N
147	MK	country.MK	\N
148	ML	country.ML	\N
149	MM	country.MM	\N
150	MN	country.MN	\N
151	MO	country.MO	\N
152	MP	country.MP	\N
153	MQ	country.MQ	\N
154	MR	country.MR	\N
155	MS	country.MS	\N
156	MT	country.MT	\N
157	MU	country.MU	\N
158	MV	country.MV	\N
159	MW	country.MW	\N
160	MX	country.MX	\N
161	MY	country.MY	\N
162	MZ	country.MZ	\N
163	NA	country.NA	\N
164	NC	country.NC	\N
165	NE	country.NE	\N
166	NF	country.NF	\N
167	NG	country.NG	\N
168	NI	country.NI	\N
169	NL	country.NL	\N
170	NO	country.NO	\N
171	NP	country.NP	\N
172	NR	country.NR	\N
173	NT	country.NT	\N
174	NU	country.NU	\N
175	NZ	country.NZ	\N
176	OM	country.OM	\N
177	PA	country.PA	\N
178	PE	country.PE	\N
179	PF	country.PF	\N
180	PG	country.PG	\N
181	PH	country.PH	\N
182	PK	country.PK	\N
183	PL	country.PL	\N
184	PM	country.PM	\N
185	PN	country.PN	\N
186	PR	country.PR	\N
187	PT	country.PT	\N
188	PW	country.PW	\N
189	PY	country.PY	\N
190	QA	country.QA	\N
191	RE	country.RE	\N
192	RO	country.RO	\N
193	RU	country.RU	\N
194	RW	country.RW	\N
195	SA	country.SA	\N
196	SC	country.SC	\N
197	SD	country.SD	\N
198	SE	country.SE	\N
199	SG	country.SG	\N
200	SH	country.SH	\N
201	SI	country.SI	\N
202	SK	country.SK	\N
203	SL	country.SL	\N
204	SM	country.SM	\N
205	SN	country.SN	\N
206	SO	country.SO	\N
207	SR	country.SR	\N
208	ST	country.ST	\N
209	SU	country.SU	\N
210	SV	country.SV	\N
211	SY	country.SY	\N
212	SZ	country.SZ	\N
213	Sb	country.Sb	\N
214	TC	country.TC	\N
215	TD	country.TD	\N
216	TG	country.TG	\N
217	TH	country.TH	\N
218	TJ	country.TJ	\N
219	TK	country.TK	\N
220	TM	country.TM	\N
221	TN	country.TN	\N
222	TO	country.TO	\N
223	TP	country.TP	\N
224	TR	country.TR	\N
225	TT	country.TT	\N
226	TV	country.TV	\N
227	TW	country.TW	\N
228	TZ	country.TZ	\N
229	UA	country.UA	\N
230	UG	country.UG	\N
231	UK	country.UK	\N
232	UM	country.UM	\N
233	US	country.US	\N
234	UY	country.UY	\N
235	UZ	country.UZ	\N
236	VC	country.VC	\N
237	VE	country.VE	\N
238	VG	country.VG	\N
239	VI	country.VI	\N
240	VN	country.VN	\N
241	VU	country.VU	\N
242	WF	country.WF	\N
243	WS	country.WS	\N
244	YE	country.YE	\N
245	YT	country.YT	\N
246	YU	country.YU	\N
247	ZA	country.ZA	\N
248	ZM	country.ZM	\N
249	ZW	country.ZW	\N
250	CB	country.CB	\N
251	VA	country.VA	\N
\.


--
-- Data for TOC entry 139 (OID 135236)
-- Name: calendar_event; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY calendar_event (id, "start", finish, subject, description, dayevent, modified, person_user, "type", created, modified_by) FROM stdin;
\.


--
-- Data for TOC entry 140 (OID 135246)
-- Name: calendar_holiday; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY calendar_holiday (id, date, name, description) FROM stdin;
\.


--
-- Data for TOC entry 141 (OID 135256)
-- Name: directory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY directory (id, person_user, name, parent, modified) FROM stdin;
0	1	root	\N	2002-10-01 00:00:00
1	1	library	0	2002-10-01 00:00:00
2	1	1	1	2002-10-01 00:00:00
\.


--
-- Data for TOC entry 142 (OID 135263)
-- Name: file; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY file (id, person_user, file_name, mime_type, head_revision, modified, size, directory) FROM stdin;
\.


--
-- Data for TOC entry 143 (OID 135270)
-- Name: high_id; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY high_id (id, name, high_id, modified) FROM stdin;
1	Address	1000	\N
2	AddressCountry	1000	\N
3	CalendarEvent	1000	\N
4	CalendarHoliday	1000	\N
33	Directory	1000	\N
5	File	1000	\N
6	LibraryComment	1000	\N
7	LibraryFaq	1000	\N
8	LibraryFaqCategory	1000	\N
9	LibraryItem	1000	\N
10	LibraryPage	1000	\N
11	LibraryTopic	1000	\N
12	Message	1000	\N
13	Meeting	1000	\N
14	MeetingAgendaPoint	1000	\N
15	MeetingAttendee	1000	\N
16	MeetingCategory	1000	\N
17	NavigationFolder	1000	\N
18	NavigationNenu	1000	\N
19	NavigationMenuItem	1000	\N
20	Person	1000	\N
21	PersonEmployee	1000	\N
22	PersonGroup	1000	\N
23	PersonGroupMember	1000	\N
24	PersonGroupRight	1000	\N
25	PersonGroupRightDetail	1000	\N
26	PersonUser	1000	\N
27	PublicHoliday	1000	\N
28	SearchIndex	1000	\N
29	SearchResult	1000	\N
30	SearchStopWord	1000	\N
31	Setting	1000	\N
32	TelecomAddress	1000	\N
\.


--
-- Data for TOC entry 144 (OID 135277)
-- Name: library_comment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY library_comment (id, id_reply_to, person_user, item, subject, text, modified, format, no_reply) FROM stdin;
\.


--
-- Data for TOC entry 145 (OID 135287)
-- Name: library_faq; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY library_faq (id, library_faq_category, question, answer) FROM stdin;
1	1	What is this document?	<p>This document tries to answer frequently asked questions about ivata groupware.</p>
6	2	What browser do I need?	<p>There is no specific browser that we use, though most of the pages have been thoroughly tested with <a href="http://www.mozilla.org/">Mozilla</a>, and the <a href="http://www.microsoft.com/windows/ie">Internet Explorer</a>.</p>
7	2	Can people outside the company see my article on the internet?	<p style="font-style: normal; font-weight: medium">No. Every page is protected by the login process.</p>
11	3	How long should my item be?	<p>As long as it needs to be <strong>:-)</strong></p> <p style="font-weight: medium">We have items ranging from just a few sentences to tens of pages. The only exception to this is please don&#146;t post two short items one after another, where the subject content is similar in each case.</p>
9	3	What subject can I write about?	<p>Well anything that is to do with your company. Looking at some of the existing items should give you some ideas.</p>
18	4	On which items may I post comments?	<p>You may and should post comments on all items about which you have a feeling. Go for it! Tell us what you really think!</p>
20	4	What do I do if I have a really long comment? Should I post a new item?	<p>That depends. If the original item was written a while ago and you have some additional information (rather than just an opinion) then it might make sense to post a new item. Otherwise, post the comment to the item directly. Long comments are automatically shortened with a little link at the end of the comment to show the whole text.</p>
2	1	What is ivata groupware?	<p>ivata groupware is a series of pages which are internal to your company. It's a private site where the firm can share information, and record progress as a team.</p>
8	3	May I write an item to the library?	<p>Yes, yes, yes! Everyone who has access to ivata groupware should feel free to post articles about matters of interest to the company.</p>
19	4	How will people know I have written a comment?	<p>Whenever you send a comment to an item, all users of your company ivata groupware automatically receive an email containing the comment and a link to the item it is about. If you reply to an original comment, then the comment you are replying to is also shown in the email.</p> <p><strong>Note:</strong> only the users of  <strong><u>your</u></strong> site will see the comment. Other companies who also have ivata accounts  have no acess to your information.</p>
\.


--
-- Data for TOC entry 146 (OID 135297)
-- Name: library_faq_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY library_faq_category (id, library_item, name, description) FROM stdin;
1	1	general	This section contains the most basic questions about using the ivata groupware.
2	1	security &amp; access	All matters concerning accessing the system and who can see what you write are answered here.
3	1	library items	Questions about submitting library items, and the sort of content they should offer.
4	1	comments	Look here for the who, what when and why of comment posting.
\.


--
-- Data for TOC entry 147 (OID 135307)
-- Name: library_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY library_item (id, "type", person_user, library_topic, title, image_directory, created_by, created, modified_by, modified, summary) FROM stdin;
1	2	1	7	Frequently Asked Questions	\N	1	2001-06-06 10:00:00	1	2002-10-01 00:00:00	Frequently Asked Questions about this ivata groupware
\.


--
-- Data for TOC entry 148 (OID 135317)
-- Name: library_page; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY library_page (id, library_item, number, text) FROM stdin;
\.


--
-- Data for TOC entry 149 (OID 135327)
-- Name: library_topic; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY library_topic (id, image, caption) FROM stdin;
1	project_plan.gif	Project Plan
2	jokes.gif	Jokes
3	consulting.gif	Consulting
4	business_news.gif	Business news
5	financial_reporting.gif	Financial Reporting
6	meeting.gif	Meetings
7	system_administration.gif	System Administration
8	technical_news.gif	Technical News
9	executive_reports.gif	Executive Reports
10	group_event.gif	Group Event
11	company_news.gif	Company News
12	office_administration.gif	Office Administration
\.


--
-- Data for TOC entry 150 (OID 135337)
-- Name: mail_message; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mail_message (id) FROM stdin;
\.


--
-- Data for TOC entry 151 (OID 135344)
-- Name: meeting; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY meeting (id, calendar_event, library_item, "location", chair_person) FROM stdin;
\.


--
-- Data for TOC entry 152 (OID 135351)
-- Name: meeting_agenda_point; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY meeting_agenda_point (id, category, name, minutes_text) FROM stdin;
\.


--
-- Data for TOC entry 153 (OID 135361)
-- Name: meeting_attendee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY meeting_attendee (id, person, meeting, confirmed) FROM stdin;
\.


--
-- Data for TOC entry 154 (OID 135368)
-- Name: meeting_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY meeting_category (id, meeting, name) FROM stdin;
\.


--
-- Data for TOC entry 155 (OID 135375)
-- Name: navigation_folder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY navigation_folder (id, folder, open, tree, person_user) FROM stdin;
\.


--
-- Data for TOC entry 156 (OID 135382)
-- Name: navigation_menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY navigation_menu (id, priority, text) FROM stdin;
1	-1	main
2	10	homepages
4	2	library
0	0	favorites
3	1	calendar
5	3	webmail
\.


--
-- Data for TOC entry 157 (OID 135389)
-- Name: navigation_menu_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY navigation_menu_item (id, person_user, priority, menu, text, url, image) FROM stdin;
44	\N	0	1	calendar	calendar	calendar/index.gif
45	\N	1	1	contacts	addressBook	addressBook/index.gif
46	\N	2	1	library	library	library/index.gif
47	\N	3	1	webmail	mail	mail/index.gif
48	\N	98	1	preferences	popUpWindow:setting?width=600&height=550	setting/index.gif
49	\N	99	1	faq	library/display.action?id=1	library/faq.gif
50	\N	100	1	logout	logout.jsp	logout.gif
51	\N	0	3	new event	popUpWindow:calendar/event.action?type=1&width=550&height=520	calendar/event.gif
53	\N	-1	4	notice board	library	library/index.gif
54	\N	0	4	new document	library/submit.action?type=0	library/submitDocument.gif
55	\N	1	4	make note	library/submit.action?type=4	library/submitNotice.gif
56	\N	2	4	post answers	library/submit.action?type=2	library/submitFaq.gif
57	\N	3	4	write minutes	popUpWindow:calendar/minutesForMeeting.jsp	library/submitMinutes.gif
58	\N	100	4	topics	library/topic.jsp	library/topics.gif
59	\N	101	4	all items	library/topicItems.jsp	library/topicItems.gif
65	\N	4	1	drive	drive	drive/index.gif
60	\N	1	5	check mail	mail/index.jsp?folderName=inbox	mail/checkMail.gif
61	\N	2	5	compose	popUpWindow:mail/compose.action?width=550&height=540	mail/compose.gif
62	\N	3	5	drafts	mail/index.jsp?folderName=drafts	mail/drafts.gif
63	\N	4	5	sent folder	mail/index.jsp?folderName=sent	mail/sentMail.gif
64	\N	100	5	trash	mail/index.jsp?folderName=trash	mail/trash.gif
\.


--
-- Data for TOC entry 158 (OID 135396)
-- Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY person (id, first_names, last_name, date_of_birth, company, person_group, file_as, salutation, job_title, created_by) FROM stdin;
1	admin	admin	\N	\N	7	admin	\N	\N	1
\.


--
-- Data for TOC entry 159 (OID 135403)
-- Name: person_employee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY person_employee (id, person, address_country, region_code, vacation_days, number) FROM stdin;
\.


--
-- Data for TOC entry 160 (OID 135411)
-- Name: person_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY person_group (id, name, head, parent, description, created_by) FROM stdin;
0	groups	\N	\N	groups	1
2	everyone	\N	0	Every user belongs to this group, contain other user groups	1
1	administrator	\N	2	Every user who is administrator belongs to this group.	1
3	private_user	\N	0	This group contain all private groups of each user	1
4	address_book	\N	0	This group contain all private ABs and all public ABs	1
5	private contacts	\N	4	This group contain all private address books for each user	1
6	shared contacts	\N	4	This group contain all public address books	1
7	public	\N	6	This is the addressbook everyone can see.	1
500	admin	\N	3	Admin private group.	1
501	admin	\N	5	Admin user private addresses.	1
\.


--
-- Data for TOC entry 161 (OID 135418)
-- Name: person_group_member; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY person_group_member (person_user, person_group) FROM stdin;
1	1
1	2
1	500
\.


--
-- Data for TOC entry 162 (OID 135422)
-- Name: person_group_right; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY person_group_right (id, target_id, person_group, detail, "access") FROM stdin;
1	1	1	3	0
2	1	1	3	1
3	1	1	3	2
4	1	1	3	3
5	1	1	2	0
6	1	1	2	1
7	1	1	2	2
8	1	1	2	3
9	2	1	3	0
10	2	1	3	1
11	2	1	3	2
12	2	1	3	3
13	2	1	2	0
14	2	1	2	1
15	2	1	2	2
16	2	1	2	3
17	3	1	3	0
18	3	1	3	1
19	3	1	3	2
20	3	1	3	3
21	3	1	2	0
22	3	1	2	1
23	3	1	2	2
24	3	1	2	3
33	4	1	3	0
34	4	1	3	1
35	4	1	3	2
36	4	1	3	3
37	4	1	2	0
38	4	1	2	1
39	4	1	2	2
40	4	1	2	3
41	5	1	3	0
42	5	1	3	1
43	5	1	3	2
44	5	1	3	3
45	5	1	2	0
46	5	1	2	1
47	5	1	2	2
48	5	1	2	3
49	6	1	3	0
50	6	1	3	1
51	6	1	3	2
52	6	1	3	3
53	6	1	2	0
54	6	1	2	1
55	6	1	2	2
56	6	1	2	3
57	7	1	3	0
58	7	1	3	1
59	7	1	3	2
60	7	1	3	3
61	7	1	2	0
62	7	1	2	1
63	7	1	2	2
64	7	1	2	3
65	8	1	3	0
66	8	1	3	1
67	8	1	3	2
68	8	1	3	3
69	8	1	2	0
70	8	1	2	1
71	8	1	2	2
72	8	1	2	3
73	9	1	3	0
74	9	1	3	1
75	9	1	3	2
76	9	1	3	3
77	9	1	2	0
78	9	1	2	1
79	9	1	2	2
80	9	1	2	3
81	10	1	3	0
82	10	1	3	1
83	10	1	3	2
84	10	1	3	3
85	10	1	2	0
86	10	1	2	1
87	10	1	2	2
88	10	1	2	3
89	11	1	3	0
90	11	1	3	1
91	11	1	3	2
92	11	1	3	3
93	11	1	2	0
94	11	1	2	1
95	11	1	2	2
96	11	1	2	3
158	12	1	3	0
159	12	1	3	1
160	12	1	3	2
161	12	1	3	3
162	12	1	2	0
163	12	1	2	1
164	12	1	2	2
165	12	1	2	3
170	1	2	3	0
171	1	2	2	0
172	1	2	2	1
173	1	2	2	2
174	1	2	2	3
175	2	2	3	0
176	2	2	2	0
177	2	2	2	1
178	2	2	2	2
179	2	2	2	3
180	3	2	3	0
181	3	2	2	0
182	3	2	2	1
183	3	2	2	2
184	3	2	2	3
185	4	2	3	0
186	4	2	2	0
187	4	2	2	1
188	4	2	2	2
189	4	2	2	3
190	5	2	3	0
191	5	2	2	0
192	5	2	2	1
193	5	2	2	2
194	5	2	2	3
195	6	2	3	0
196	6	2	2	0
197	6	2	2	1
198	6	2	2	2
199	6	2	2	3
200	7	2	3	0
201	7	2	2	0
202	7	2	2	1
203	7	2	2	2
204	7	2	2	3
205	8	2	3	0
206	8	2	2	0
207	8	2	2	1
208	8	2	2	2
209	8	2	2	3
210	9	2	3	0
211	9	2	2	0
212	9	2	2	1
213	9	2	2	2
214	9	2	2	3
215	10	2	3	0
216	10	2	2	0
217	10	2	2	1
218	10	2	2	2
219	10	2	2	3
220	11	2	3	0
221	11	2	2	0
222	11	2	2	1
223	11	2	2	2
224	11	2	2	3
225	12	2	3	0
226	12	2	2	0
227	12	2	2	1
228	12	2	2	2
229	12	2	2	3
97	1	1	1	0
98	1	1	1	1
99	1	1	1	2
100	1	1	1	3
105	2	1	1	0
106	2	1	1	1
107	2	1	1	2
108	2	1	1	3
258	2	2	1	0
259	1	2	1	0
300	7	2	1	0
301	7	2	1	1
302	7	2	1	2
303	7	2	1	3
304	7	1	1	0
305	7	1	1	1
306	7	1	1	2
307	7	1	1	3
254	501	500	1	0
255	501	500	1	1
256	501	500	1	2
257	501	500	1	3
109	\N	1	6	2
114	1	1	4	0
115	1	1	4	1
116	1	1	4	2
117	1	1	4	3
118	2	1	4	0
119	2	1	4	1
120	2	1	4	2
121	2	1	4	3
122	3	1	4	0
123	3	1	4	1
124	3	1	4	2
125	3	1	4	3
126	4	1	4	0
127	4	1	4	1
128	4	1	4	2
129	4	1	4	3
130	5	1	4	0
131	5	1	4	1
132	5	1	4	2
133	5	1	4	3
134	6	1	4	0
135	6	1	4	1
136	6	1	4	2
137	6	1	4	3
138	7	1	4	0
139	7	1	4	1
140	7	1	4	2
141	7	1	4	3
142	8	1	4	0
143	8	1	4	1
144	8	1	4	2
145	8	1	4	3
146	9	1	4	0
147	9	1	4	1
148	9	1	4	2
149	9	1	4	3
150	10	1	4	0
151	10	1	4	1
152	10	1	4	2
153	10	1	4	3
154	11	1	4	0
155	11	1	4	1
156	11	1	4	2
157	11	1	4	3
166	12	1	4	0
167	12	1	4	1
168	12	1	4	2
169	12	1	4	3
260	0	1	8	0
261	0	1	8	1
262	0	1	8	2
263	0	1	8	3
264	0	2	8	0
265	0	2	8	1
266	0	2	8	2
267	0	2	8	3
268	1	1	8	0
269	1	1	8	1
270	1	1	8	2
271	1	1	8	3
272	1	2	8	0
273	1	2	8	1
274	1	2	8	2
275	1	2	8	3
276	2	1	8	0
277	2	1	8	1
278	2	1	8	2
279	2	1	8	3
280	2	2	8	0
281	2	2	8	1
282	2	2	8	2
283	2	2	8	3
\.


--
-- Data for TOC entry 163 (OID 135429)
-- Name: person_group_right_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY person_group_right_detail (id, name, description) FROM stdin;
1	PERSON_GROUP_MEMBER                     	view, amend, add or delete group memberships. This applies both to people within groups and groups within groups.
2	LIBRARY_ITEM_TOPIC                      	view, amend, add or delete library items on the basis of their topic.
3	LIBRARY_TOPIC                           	add, ammend or view topics
4	LIBRARY_COMMENT_TOPIC                   	add amend or remove comments, based on the topic of their items
5	SETTING_USER                            	amend user setting
6	SETTING_SYSTEM                          	amend system setting
8	DIRECTORY                               	view, amend, remove and add to directories
\.


--
-- Data for TOC entry 164 (OID 135439)
-- Name: person_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY person_user (id, name, person, "password", enable, deleted) FROM stdin;
1	admin	1	\N	t	f
\.


--
-- Data for TOC entry 165 (OID 135446)
-- Name: public_holiday; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public_holiday (id, calendar_event, country, region_code) FROM stdin;
\.


--
-- Data for TOC entry 166 (OID 135453)
-- Name: search_index; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY search_index (id, topic, "type", target_id, item, word, weight) FROM stdin;
\.


--
-- Data for TOC entry 167 (OID 135460)
-- Name: search_stopword; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY search_stopword (id, word) FROM stdin;
1	a
2	about
3	above
4	across
5	after
6	afterwards
7	again
8	against
9	all
10	almost
11	alone
12	along
13	already
14	also
15	although
16	always
17	am
18	among
19	amongst
20	amoungst
21	amount
22	an
23	and
24	another
25	any
26	anyhow
27	anyone
28	anything
29	anyway
30	anywhere
31	are
32	around
33	as
34	at
35	back
36	be
37	became
38	because
39	become
40	becomes
41	becoming
42	been
43	before
44	beforehand
45	behind
46	being
47	below
48	beside
49	besides
50	between
51	beyond
52	bill
53	both
54	bottom
55	but
56	by
57	call
58	can
59	cannot
60	cant
61	co
62	computer
63	con
64	could
65	couldnt
66	cry
67	de
68	describe
69	detail
70	do
71	done
72	down
73	due
74	during
75	each
76	eg
77	eight
78	either
79	eleven
80	else
81	elsewhere
82	empty
83	enough
84	etc
85	even
86	ever
87	every
88	everyone
89	everything
90	everywhere
91	except
92	few
93	fifteen
94	fify
95	fill
96	find
97	fire
98	first
99	five
100	for
101	former
102	formerly
103	forty
104	found
105	four
106	from
107	front
108	full
109	further
110	get
111	give
112	go
113	had
114	has
115	hasnt
116	have
117	he
118	hence
119	her
120	here
121	hereafter
122	hereby
123	herein
124	hereupon
125	hers
126	herself
127	him
128	himself
129	his
130	how
131	however
132	hundred
133	i
134	ie
135	if
136	in
137	inc
138	indeed
139	interest
140	into
141	is
142	it
143	its
144	itself
145	keep
146	last
147	latter
148	latterly
149	least
150	less
151	ltd
152	made
153	many
154	may
155	me
156	meanwhile
157	might
158	mill
159	mine
160	more
161	moreover
162	most
163	mostly
164	move
165	much
166	must
167	my
168	myself
169	name
170	namely
171	neither
172	never
173	nevertheless
174	next
175	nine
176	no
177	nobody
178	none
179	noone
180	nor
181	not
182	nothing
183	now
184	nowhere
185	of
186	off
187	often
188	on
189	once
190	one
191	only
192	onto
193	or
194	other
195	others
196	otherwise
197	our
198	ours
199	ourselves
200	out
201	over
202	own
203	part
204	per
205	perhaps
206	please
207	put
208	rather
209	re
210	same
211	see
212	seem
213	seemed
214	seeming
215	seems
216	serious
217	several
218	she
219	should
220	show
221	side
222	since
223	sincere
224	six
225	sixty
226	so
227	some
228	somehow
229	someone
230	something
231	sometime
232	sometimes
233	somewhere
234	still
235	such
236	system
237	take
238	ten
239	than
240	that
241	the
242	their
243	them
244	themselves
245	then
246	thence
247	there
248	thereafter
249	thereby
250	therefore
251	therein
252	thereupon
253	these
254	they
255	thick
256	thin
257	third
258	this
259	those
260	though
261	three
262	through
263	throughout
264	thru
265	thus
266	to
267	together
268	too
269	top
270	toward
271	towards
272	twelve
273	twenty
274	two
275	un
276	under
277	until
278	up
279	upon
280	us
281	very
282	via
283	was
284	we
285	well
286	were
287	what
288	whatever
289	when
290	whence
291	whenever
292	where
293	whereafter
294	whereas
295	whereby
296	wherein
297	whereupon
298	wherever
299	whether
300	which
301	while
302	whither
303	who
304	whoever
305	whole
306	whom
307	whose
308	why
309	will
310	with
311	within
312	without
313	would
314	yet
315	you
316	your
317	yours
318	yourself
319	yourselves
\.


--
-- Data for TOC entry 168 (OID 135467)
-- Name: search_result; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY search_result (id) FROM stdin;
\.


--
-- Data for TOC entry 169 (OID 135474)
-- Name: setting; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY setting (id, name, value, "type", person_user, description, enable) FROM stdin;
0	calendarDefaultView	0	0	\N	number of the default view to show in the calendar (defined in IndexFormConstants)	t
1	calendarFinishHour	18	0	\N	finish hour of views in calendar	t
2	calendarFirstWeekDay	1	0	\N	0 for sunday, 1 for monday	t
3	calendarStartHour	7	0	\N	start hour of views in calendar	t
4	demoVersion	false	2	\N	if this application is demo	t
5	emailAttachmentSizeLimit	1500	0	\N	The maximum size of an attachment in kB	t
6	emailAuthentication	false	2	\N	use email host to authenticate if true; otherwise use database	t
7	emailDebug	true	2	\N	provide debug information on logging in	t
8	emailDefaultFormat	0	0	\N	Constant format as defined in ...jsp.format.FormatConstants	t
9	emailEnable	true	2	\N	Setting this to false will disable sending mails completely.	t
10	emailFolderDrafts	drafts	1	\N	this mail folder will store the precomposed messages, drafts	f
11	emailFolderInbox	Inbox	1	\N	The name of inbox folder	f
12	emailFolderSent	sent	1	\N	folder for storing sented mails	f
13	emailFolderTrash	Trash	1	\N	The name of trash folder	f
14	emailHeaderForwardHtml	\n\n\n <table cellpadding='5' cellspacing='0' border='0'>\n <tr><td bgcolor='#cccccc' colspan='2'><div align='center'>-----Forwarded Message-----</div></td></tr>\n  <tr><td bgcolor='#cccccc'>From:</td><td>{0}</tr>\n  <tr><td bgcolor='#cccccc'>To:</td><td>{1}</tr>\n  <tr><td bgcolor='#cccccc'>Subject:</td><td>{2}</tr>\n  <tr><td bgcolor='#cccccc'>Date:</td><td>{3}</tr>\n  </table><br/>\n	1	\N	Header applied to forwarded emails in HTML format. Use {0} for the sender(s), {1} for the recipients (to, not cc/bcc), {2} for the message subject, {3} for the sent date.	t
15	emailHeaderForwardText	\n\n\n-----Forwarded Message-----\n From: {0}\n To: {1}\n Subject: {2}\n Date: {3}\n	1	\N	Header applied to forwarded emails. Use {0} for the sender(s), {1} for the recipients (to, not cc/bcc), {2} for the message subject, {3} for the sent date.	t
16	emailHeaderReplyHtml	\n\n\nOn {3}, {0} wrote:<br/>\n	1	\N	Header applied to replied emails in HTML format. Use {0} for the sender(s), {1} for the recipients (to, not cc/bcc), {2} for the message subject, {3} for the sent date.	t
17	emailHeaderReplyText	\n\n\nOn {3}, {0} wrote:\n\n	1	\N	Header applied to forwarded emails. Use {0} for the sender(s), {1} for the recipients (to, not cc/bcc), {2} for the message subject, {3} for the sent date.	t
18	emailHost	localhost	1	\N	name/ip address of the mail server	f
19	emailHostSmtp	localhost	1	\N	the host to use for sending emails	f
20	emailLibrary	webmaster@localhost	1	\N	this is the special user on the email system who will recieve all replies to notifications about library submissions.	t
21	emailLibraryPassword	library	1	\N	password to log in as the library user	t
22	emailLibraryUser	library	1	\N	username for replies to the library system	t
23	emailPersonalNamespace	INBOX	1	\N	Some IMAP servers (read: courier) provide shared and personal namespaces. If specified (not empty), this is this the name of the namespace to use for personal folders. Drafts, sent and trash will all be created in this folder. The inbox depends on the setting emailInboxPath, which is taken to be an absolute path from the store.	t
24	emailPort	143	0	\N	email port used to connect	f
25	emailReplyIndent	>	1	\N	The character(s) to prepend to each line of forward or reply emails	t
26	emailScriptServerEnvironment	SUDO_USER=root\nSUDO_PATH=/usr/local/ivataop/mailServer/exim\nSITE_ID=www	1	\N	this contains all environment variables for the script mail server. there should be combinations of name=value pairs, seperated by the new line character ( n)	t
27	emailSignatureForward	true	2	\N	If true, appends signatures to forwarded emails.	t
28	emailSignatureHtml	<BR/><HR/><P>Composed using ivata groupware - team communication software.<br/>Visit <A HREF='http://www.ivata.com'>www.ivata.com</A> for details	1	\N	The text to automatically appear at the foot of composed emails (HTML format).	t
29	emailSignatureReply	true	2	\N	If true, appends signatures to replies.	t
30	emailSignatureText	__________________________________________________\ncomposed using ivata groupware - team communication\nsoftware. Visit http://www.ivata.com for details.	1	\N	The text to automatically appear at the foot of composed emails. (Text Format).	t
31	emailSubjectForwardPrefix	Fwd: 	1	\N	This string is prepended to the subject lines of forwarded messages.	t
32	emailSubjectForwardSeperator	; 	1	\N	This string seperates multiple forwarded emails.	t
33	emailSubjectReplyPrefix	Re: 	1	\N	This string is prepended to the subject lines of replied to messages.	t
34	emailTitle	ivata mail	1	\N	title used in the email windows	t
35	fileFolderTemporary	/tmp	1	\N	all the temporary files and directories will be stored here	t
36	fileMaxSize	524288000	0	\N	maximum file upload size	t
37	groupAdministrator	1	0	\N	administrator group = complete access over entire system	t
38	guiButtonSpacer	10	0	\N	This is the space between button groups under each window	t
39	guiMenuCharWidth	10	0	\N	width allowed for each character in menues	t
40	guiMenuItemHeight	25	0	\N	height allowed for each menu item	t
41	guiMenuItemOffset	10	0	\N	offset before all the menu items to try to fix MSIE	t
42	guiMinimumTextareaHeight	80	0	\N	Setting this to the minimum size allowed for textarea fields.	t
43	guiShortcutPause	500	0	\N	How long to wait between move over events, for dynamic shortcuts.	t
44	guiShortcutStyle	2	0	\N	Dictates how the shortcut menues (on the left hand side) behave.	t
45	guiToolBarSpacerWidth	20	0	\N	width of a spacer in the toolbar	t
46	i18nDateInput	yyyy-M-d	1	\N	This is the input mask for entering dates	t
47	i18nDateInputDisplay	yyyy-MM-dd	1	\N	The format of the dates after the user entered them to a date field.	t
48	i18nDateLastWeekDay	'last' EEEE	1	\N	this is the SimpleDateFormat text used for relative dates when the date compared comes on the previous week of the current date	t
49	i18nDateLong	d MMMM	1	\N	this is the long SimpleDateFormat text used for long dates which need not show the year	t
50	i18nDateLongDay	EEEE, d MMMM	1	\N	this is a long SimpleDateFormat test used for long dates which need to show the working day	t
51	i18nDateLongYear	d MMMM yyyy	1	\N	this is the long SimpleDateFormat text used for long dates which need to show the year	t
52	i18nDateShort	d MMM	1	\N	this is the short date SimpleDateFormat text used, just containing numbers	t
53	i18nDateShortToday	'Today'	1	\N	date to use in short relative date format for today.	t
54	i18nDateShortYear	yyyy-MM-dd	1	\N	this is the short date SimpleDateFormat text used, just containing numbers	t
55	i18nDateShortYesterday	'Yesterday'	1	\N	date to use in short relative date format for yesterday.	t
56	i18nDateThisAfternoon	'This afternoon'	1	\N	this is the SimpleDateFormat text used for relative dates when the date compared comes on the afternoon of the current date	t
57	i18nDateThisEvening	'This evening'	1	\N	this is the SimpleDateFormat text used for relative dates when the date compared comes on the eveing of the current date	t
58	i18nDateThisMorning	'This morning'	1	\N	this is the SimpleDateFormat text used for relative dates when the date compared comes on the morning of the current date	t
59	i18nDateWeekDay	EEEE	1	\N	this is the SimpleDateFormat text used for relative dates when the date compared comes on the week of the current date	t
60	i18nDateYesterdayAfternoon	'Yesterday afternoon'	1	\N	this is the SimpleDateFormat text used for relative dates when the date compared comes on the afernoon of the day before the current date	t
61	i18nDateYesterdayEvening	'Last night'	1	\N	this is the SimpleDateFormat text used for relative dates when the date compared comes on the evening of the day before the current date	t
62	i18nDateYesterdayMorning	'Yesterday morning'	1	\N	this is the SimpleDateFormat text used for relative dates when the date compared comes on the morning of the day before the current date	t
63	i18nEncoding	utf-8	1	\N	character set encoding used	t
64	i18nLanguage	en	1	\N	language to be used throughout the system. currently, this feature is not used.	t
65	i18nLocaleCountry	us	1	\N	locale country code (see http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html)	t
66	i18nLocaleLanguage	en	1	\N	locale language code (see http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt)	t
67	i18nTimeInput	H:m	1	\N	The format of the time input.	t
68	i18nTimeInputDisplay	HH:mm	1	\N	The format of time displayed in a time input field after the user entered it.	t
69	i18nTimeLong	h:mm:ss a	1	\N	this is the long time SimpleDateFormat text including seconds	t
70	i18nTimeShort	h:mm a	1	\N	this is the short time SimpleDateFormat text without seconds	t
71	i18nTimeZone	Europe/London	1	\N	Current timezone used to display times and dates.	t
73	libraryCommentLength	3000	0	\N	number of bytes shown under article b4 its cut short with a full view link	t
74	libraryCommentSpacer	30	0	\N	the number of pixels to indent each level of the library comment tree	t
75	libraryHome	5	0	\N	number of articles to show on the homepage	t
76	libraryNotificationContentComment	<p><em>To {1}: </em>{0} has replied to your comment:<br/><em>New Comment:</em><br/><strong>{4}</strong><br/>{5}<br/><br/><em>Original comment:</em><br/><strong>{2}</strong><br/>{3}</p>	1	\N	this is the string surrounding the notification for a comment. Use {0} for the user name of the original comment author, {1} for the author of the new comment, {2} for the title of the original comment, {3} for the content of the original comment, {4} for the comment subject, {5} for the (HTMLified) text of the comment and {6} for the comment url.	t
77	libraryNotificationContentCommentAmend	<p><em>to {1}: </em>{0} has amended the reply to your comment: <br/><em>Amened Reply:</em><br/><strong>{4}</strong><br/>{5}<br/><br/><em>as Reply To:</em><br/><strong>{2}</strong><br/>{3}</p>	1	\N	1	t
78	libraryNotificationContentCommentAmendStrange	<p><em>to {1}: </em>{0} has amended your comment entitled <strong>{2}</strong>.</p>	1	\N	this used when user did change not his comment	t
79	libraryNotificationContentItem	<p>{0} has written a new library item entitled <strong>{1}</strong>:<br/>{2}</p>	1	\N	this is the string surrounding notification of a new library item. Use {0} for the user name of the new item author, {1} for the item title, {2} for the item summary and {3} for the item url.	t
80	libraryNotificationContentItemAmend	<p>{0} has amended the library item entitled <strong>{1}</strong>:<br/>{2}</p>	1	\N	this is the string surrounding notification of an amended library item. Use {0} for the user name of the new item author, {1} for the item title, {2} for the item summary and {3} for the item url.	t
81	libraryNotificationContentItemReply	<p><em>To {1}: </em>{0} has commented on your library item:<br/><em>New Comment:</em><br/><strong>{4}</strong><br/>{5}<br/><br/><em>Library Item Summary:</em><br/><strong>{2}</strong><br/>{3}</p>	1	\N	this is the string surrounding the notification for a new comment thread in reply to a library item. Use {0} for the user name of the original author, {1} for the author of the new comment, {2} for the title of the original library item, {3}for the summary of the library item, {4} for the subject of the comment, {5} (HTMLified) text of the comment and {6} for the comment url.	t
82	libraryNotificationContentItemReplyAmend	<p><em>To {1}: </em>{0} has amended the comment on your library item:<br/><em>Amended Comment:</em><br/><strong>{4}</strong><br/>{5}<br/><br/><em>Library Item Summary:</em><br/><strong>{2}</strong><br/>{3}</p>	1	\N	this is the string surrounding the notification for an amended comment thread in reply to a library item. Use {0} for the user name of the original author, {1} for the author of the new comment, {2} for the title of the original library item, {3}for the summary of the library item, {4} for the subject of the comment, {5} (HTMLified) text of the comment and {6} for the comment url.	t
83	libraryNotificationContentType	text/html	1	\N	the type used in email notifications	t
84	libraryRecent	20	0	\N	number of articles to show on the recent articles list	t
85	pathButtonsDirectory	images/buttons/en/	1	\N	directory where all the buttons are stored	t
86	pathContext	http://localhost	1	\N	the stem of the url to the site	t
87	pathLibraryITemImages	/images/articles/	1	\N	path to article images	t
88	pathMenuItemImages	/images/menu/item/	1	\N	Stores the path for the short cut images, in the left frame. Note: must have a trailing slash!	t
89	pathScriptMailServer	/usr/local/ivataop/mailserver/sudo	1	\N	path for the scripts which drive the mail server	t
90	pathTopics	/library/images/topics/	1	\N	path to topic images	t
91	securitySessionServer	com.ivata.groupware.business.mail.server.ScriptMailServer	1	\N		f
92	siteAdminEmail	webmaster@localhost	1	\N	the email address for users to address any queries about the site to.	t
93	siteCheckQuota	true	2	\N	set to true to enable the quota display in the top right frame.	f
94	siteDefaultForward	mailIndex	1	\N	This is the default url which will appear when the user first logs in.	t
96	siteLogo	/images/logo.jpg	1	\N	full path to the logo for the title page	f
97	siteName	ivata groupware	1	\N	this is the name of the site as used when welcoming the user in the login screen, for example	f
98	siteQuotaLimit	2	0	\N	limit for free space of quota, in MegaBytes	t
99	siteTitle	ivata groupware	1	\N	this is the main title for the site, which is used in all the header tags	f
100	siteVersion	N/A	1	\N	current version of ivata groupware. used by the install script.	t
101	siteWelcomeText	<p>This is a members-only area, so you will have to kindly login to enjoy the full benefits of ivata groupware. <br/>   In order for this program to function properly, please make sure pop ups are enabled in your browser.    </p>	1	\N	Text which appears in the login screen. This is one setting which the users cannot override.	t
\.


--
-- Data for TOC entry 170 (OID 135484)
-- Name: telecom_address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY telecom_address (id, person, address, address_type, number) FROM stdin;
1	1	admin@site.com	4	0
\.


--
-- TOC entry 104 (OID 135225)
-- Name: address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- TOC entry 105 (OID 135232)
-- Name: address_country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address_country
    ADD CONSTRAINT address_country_pkey PRIMARY KEY (id);


--
-- TOC entry 106 (OID 135242)
-- Name: calendar_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY calendar_event
    ADD CONSTRAINT calendar_event_pkey PRIMARY KEY (id);


--
-- TOC entry 107 (OID 135252)
-- Name: calendar_holiday_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY calendar_holiday
    ADD CONSTRAINT calendar_holiday_pkey PRIMARY KEY (id);


--
-- TOC entry 108 (OID 135259)
-- Name: directory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY directory
    ADD CONSTRAINT directory_pkey PRIMARY KEY (id);


--
-- TOC entry 109 (OID 135266)
-- Name: file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY file
    ADD CONSTRAINT file_pkey PRIMARY KEY (id);


--
-- TOC entry 110 (OID 135273)
-- Name: high_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY high_id
    ADD CONSTRAINT high_id_pkey PRIMARY KEY (id);


--
-- TOC entry 111 (OID 135283)
-- Name: library_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_comment
    ADD CONSTRAINT library_comment_pkey PRIMARY KEY (id);


--
-- TOC entry 112 (OID 135293)
-- Name: library_faq_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_faq
    ADD CONSTRAINT library_faq_pkey PRIMARY KEY (id);


--
-- TOC entry 113 (OID 135303)
-- Name: library_faq_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_faq_category
    ADD CONSTRAINT library_faq_category_pkey PRIMARY KEY (id);


--
-- TOC entry 114 (OID 135313)
-- Name: library_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_item
    ADD CONSTRAINT library_item_pkey PRIMARY KEY (id);


--
-- TOC entry 115 (OID 135323)
-- Name: library_page_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_page
    ADD CONSTRAINT library_page_pkey PRIMARY KEY (id);


--
-- TOC entry 116 (OID 135333)
-- Name: library_topic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY library_topic
    ADD CONSTRAINT library_topic_pkey PRIMARY KEY (id);


--
-- TOC entry 117 (OID 135340)
-- Name: mail_message_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mail_message
    ADD CONSTRAINT mail_message_pkey PRIMARY KEY (id);


--
-- TOC entry 118 (OID 135347)
-- Name: meeting_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (id);


--
-- TOC entry 119 (OID 135357)
-- Name: meeting_agenda_point_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY meeting_agenda_point
    ADD CONSTRAINT meeting_agenda_point_pkey PRIMARY KEY (id);


--
-- TOC entry 120 (OID 135364)
-- Name: meeting_attendee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY meeting_attendee
    ADD CONSTRAINT meeting_attendee_pkey PRIMARY KEY (id);


--
-- TOC entry 121 (OID 135371)
-- Name: meeting_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY meeting_category
    ADD CONSTRAINT meeting_category_pkey PRIMARY KEY (id);


--
-- TOC entry 122 (OID 135378)
-- Name: navigation_folder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY navigation_folder
    ADD CONSTRAINT navigation_folder_pkey PRIMARY KEY (id);


--
-- TOC entry 123 (OID 135385)
-- Name: navigation_menu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY navigation_menu
    ADD CONSTRAINT navigation_menu_pkey PRIMARY KEY (id);


--
-- TOC entry 124 (OID 135392)
-- Name: navigation_menu_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY navigation_menu_item
    ADD CONSTRAINT navigation_menu_item_pkey PRIMARY KEY (id);


--
-- TOC entry 125 (OID 135399)
-- Name: person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 126 (OID 135407)
-- Name: person_employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_employee
    ADD CONSTRAINT person_employee_pkey PRIMARY KEY (id);


--
-- TOC entry 127 (OID 135414)
-- Name: person_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_group
    ADD CONSTRAINT person_group_pkey PRIMARY KEY (id);


--
-- TOC entry 128 (OID 135425)
-- Name: person_group_right_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_group_right
    ADD CONSTRAINT person_group_right_pkey PRIMARY KEY (id);


--
-- TOC entry 129 (OID 135435)
-- Name: person_group_right_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_group_right_detail
    ADD CONSTRAINT person_group_right_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 130 (OID 135442)
-- Name: person_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person_user
    ADD CONSTRAINT person_user_pkey PRIMARY KEY (id);


--
-- TOC entry 131 (OID 135449)
-- Name: public_holiday_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public_holiday
    ADD CONSTRAINT public_holiday_pkey PRIMARY KEY (id);


--
-- TOC entry 132 (OID 135456)
-- Name: search_index_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY search_index
    ADD CONSTRAINT search_index_pkey PRIMARY KEY (id);


--
-- TOC entry 133 (OID 135463)
-- Name: search_stopword_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY search_stopword
    ADD CONSTRAINT search_stopword_pkey PRIMARY KEY (id);


--
-- TOC entry 134 (OID 135470)
-- Name: search_result_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY search_result
    ADD CONSTRAINT search_result_pkey PRIMARY KEY (id);


--
-- TOC entry 135 (OID 135480)
-- Name: setting_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY setting
    ADD CONSTRAINT setting_pkey PRIMARY KEY (id);


--
-- TOC entry 136 (OID 135487)
-- Name: telecom_address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY telecom_address
    ADD CONSTRAINT telecom_address_pkey PRIMARY KEY (id);


--
-- TOC entry 3 (OID 135220)
-- Name: address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('address_id_seq', 1, false);


--
-- TOC entry 5 (OID 135227)
-- Name: address_country_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('address_country_id_seq', 251, true);


--
-- TOC entry 7 (OID 135234)
-- Name: calendar_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('calendar_event_id_seq', 1, false);


--
-- TOC entry 9 (OID 135244)
-- Name: calendar_holiday_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('calendar_holiday_id_seq', 1, false);


--
-- TOC entry 11 (OID 135254)
-- Name: directory_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('directory_id_seq', 2, true);


--
-- TOC entry 13 (OID 135261)
-- Name: file_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('file_id_seq', 1, false);


--
-- TOC entry 15 (OID 135268)
-- Name: high_id_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('high_id_id_seq', 33, true);


--
-- TOC entry 17 (OID 135275)
-- Name: library_comment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('library_comment_id_seq', 1, false);


--
-- TOC entry 19 (OID 135285)
-- Name: library_faq_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('library_faq_id_seq', 22, true);


--
-- TOC entry 21 (OID 135295)
-- Name: library_faq_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('library_faq_category_id_seq', 5, true);


--
-- TOC entry 23 (OID 135305)
-- Name: library_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('library_item_id_seq', 1, true);


--
-- TOC entry 25 (OID 135315)
-- Name: library_page_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('library_page_id_seq', 1, false);


--
-- TOC entry 27 (OID 135325)
-- Name: library_topic_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('library_topic_id_seq', 12, true);


--
-- TOC entry 29 (OID 135335)
-- Name: mail_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('mail_message_id_seq', 1, false);


--
-- TOC entry 31 (OID 135342)
-- Name: meeting_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('meeting_id_seq', 1, false);


--
-- TOC entry 33 (OID 135349)
-- Name: meeting_agenda_point_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('meeting_agenda_point_id_seq', 1, false);


--
-- TOC entry 35 (OID 135359)
-- Name: meeting_attendee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('meeting_attendee_id_seq', 1, false);


--
-- TOC entry 37 (OID 135366)
-- Name: meeting_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('meeting_category_id_seq', 1, false);


--
-- TOC entry 39 (OID 135373)
-- Name: navigation_folder_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('navigation_folder_id_seq', 1, false);


--
-- TOC entry 41 (OID 135380)
-- Name: navigation_menu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('navigation_menu_id_seq', 5, true);


--
-- TOC entry 43 (OID 135387)
-- Name: navigation_menu_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('navigation_menu_item_id_seq', 65, true);


--
-- TOC entry 45 (OID 135394)
-- Name: person_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('person_id_seq', 1, true);


--
-- TOC entry 47 (OID 135401)
-- Name: person_employee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('person_employee_id_seq', 1, false);


--
-- TOC entry 49 (OID 135409)
-- Name: person_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('person_group_id_seq', 501, true);


--
-- TOC entry 51 (OID 135416)
-- Name: person_group_member_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('person_group_member_id_seq', 1, false);


--
-- TOC entry 53 (OID 135420)
-- Name: person_group_right_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('person_group_right_id_seq', 307, true);


--
-- TOC entry 55 (OID 135427)
-- Name: person_group_right_detail_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('person_group_right_detail_id_seq', 8, true);


--
-- TOC entry 57 (OID 135437)
-- Name: person_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('person_user_id_seq', 1, true);


--
-- TOC entry 59 (OID 135444)
-- Name: public_holiday_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('public_holiday_id_seq', 1, false);


--
-- TOC entry 61 (OID 135451)
-- Name: search_index_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('search_index_id_seq', 1, false);


--
-- TOC entry 63 (OID 135458)
-- Name: search_stopword_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('search_stopword_id_seq', 319, true);


--
-- TOC entry 65 (OID 135465)
-- Name: search_result_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('search_result_id_seq', 1, false);


--
-- TOC entry 67 (OID 135472)
-- Name: setting_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('setting_id_seq', 300, true);


--
-- TOC entry 69 (OID 135482)
-- Name: telecom_address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval ('telecom_address_id_seq', 1, true);


