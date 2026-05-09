--
-- PostgreSQL database dump
--

\restrict dgwpxy68qz7z59iIoP5ofRETF4QExhtg3evxOBrrJQevzzffqFzl0QFSx3NPLsd

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-05-09 23:48:45

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 229 (class 1259 OID 16491)
-- Name: enrollment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.enrollment (
    enrollment_id integer NOT NULL,
    student_id integer NOT NULL,
    grade_level character varying(2) NOT NULL,
    diagnostic_test_schedule date NOT NULL,
    diagnostic_test_status boolean NOT NULL,
    psa_status boolean NOT NULL,
    sf10_status boolean NOT NULL,
    goodmoral_status boolean NOT NULL,
    enrollment_status integer NOT NULL,
    payment_scheme character varying(30) DEFAULT 'QUARTERLY'::character varying NOT NULL
);


ALTER TABLE public.enrollment OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16490)
-- Name: enrollment_enrollment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.enrollment_enrollment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.enrollment_enrollment_id_seq OWNER TO postgres;

--
-- TOC entry 5083 (class 0 OID 0)
-- Dependencies: 228
-- Name: enrollment_enrollment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.enrollment_enrollment_id_seq OWNED BY public.enrollment.enrollment_id;


--
-- TOC entry 226 (class 1259 OID 16458)
-- Name: guardian; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.guardian (
    guardian_id integer NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    contact_number character varying(15) NOT NULL,
    address character varying(255) NOT NULL,
    occupation character varying(255) NOT NULL
);


ALTER TABLE public.guardian OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16457)
-- Name: guardian_guardian_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.guardian_guardian_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.guardian_guardian_id_seq OWNER TO postgres;

--
-- TOC entry 5084 (class 0 OID 0)
-- Dependencies: 225
-- Name: guardian_guardian_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.guardian_guardian_id_seq OWNED BY public.guardian.guardian_id;


--
-- TOC entry 227 (class 1259 OID 16472)
-- Name: guardian_of_student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.guardian_of_student (
    guardian_id integer NOT NULL,
    student_id integer NOT NULL,
    relationship character varying(255) NOT NULL
);


ALTER TABLE public.guardian_of_student OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16564)
-- Name: payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment (
    payment_id integer NOT NULL,
    student_id integer NOT NULL,
    amount numeric(10,2) NOT NULL,
    payment_method character varying(255) NOT NULL,
    payment_plan character varying(255) NOT NULL,
    reference_number character varying(255) NOT NULL,
    payment_date date NOT NULL,
    CONSTRAINT chk_payment_amount_positive CHECK ((amount > (0)::numeric)),
    CONSTRAINT chk_payment_method CHECK (((payment_method)::text = ANY ((ARRAY['Cash'::character varying, 'GCash'::character varying, 'Bank Transfer'::character varying, 'Check'::character varying])::text[]))),
    CONSTRAINT chk_payment_plan CHECK (((payment_plan)::text = ANY ((ARRAY['QUARTERLY'::character varying, 'SEMI_ANNUAL'::character varying, 'ANNUAL'::character varying])::text[])))
);


ALTER TABLE public.payment OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 16563)
-- Name: payment_payment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.payment_payment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.payment_payment_id_seq OWNER TO postgres;

--
-- TOC entry 5085 (class 0 OID 0)
-- Dependencies: 230
-- Name: payment_payment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.payment_payment_id_seq OWNED BY public.payment.payment_id;


--
-- TOC entry 220 (class 1259 OID 16391)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    role_id integer NOT NULL,
    role_name character varying(255) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16390)
-- Name: role_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.role_role_id_seq OWNER TO postgres;

--
-- TOC entry 5086 (class 0 OID 0)
-- Dependencies: 219
-- Name: role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_role_id_seq OWNED BY public.role.role_id;


--
-- TOC entry 224 (class 1259 OID 16441)
-- Name: student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student (
    student_id integer NOT NULL,
    grade_level character varying(2) NOT NULL,
    first_name character varying(255) NOT NULL,
    middle_name character varying(255),
    last_name character varying(255) NOT NULL,
    address character varying(255) NOT NULL,
    age integer NOT NULL,
    birthdate date NOT NULL,
    prev_school character varying(255),
    has_allergies boolean NOT NULL,
    medical_details character varying(255)
);


ALTER TABLE public.student OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16440)
-- Name: student_student_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.student_student_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_student_id_seq OWNER TO postgres;

--
-- TOC entry 5087 (class 0 OID 0)
-- Dependencies: 223
-- Name: student_student_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.student_student_id_seq OWNED BY public.student.student_id;


--
-- TOC entry 222 (class 1259 OID 16402)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16401)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_user_id_seq OWNER TO postgres;

--
-- TOC entry 5088 (class 0 OID 0)
-- Dependencies: 221
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 4889 (class 2604 OID 16494)
-- Name: enrollment enrollment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.enrollment ALTER COLUMN enrollment_id SET DEFAULT nextval('public.enrollment_enrollment_id_seq'::regclass);


--
-- TOC entry 4888 (class 2604 OID 16461)
-- Name: guardian guardian_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guardian ALTER COLUMN guardian_id SET DEFAULT nextval('public.guardian_guardian_id_seq'::regclass);


--
-- TOC entry 4891 (class 2604 OID 16567)
-- Name: payment payment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment ALTER COLUMN payment_id SET DEFAULT nextval('public.payment_payment_id_seq'::regclass);


--
-- TOC entry 4885 (class 2604 OID 16394)
-- Name: role role_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role ALTER COLUMN role_id SET DEFAULT nextval('public.role_role_id_seq'::regclass);


--
-- TOC entry 4887 (class 2604 OID 16444)
-- Name: student student_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student ALTER COLUMN student_id SET DEFAULT nextval('public.student_student_id_seq'::regclass);


--
-- TOC entry 4886 (class 2604 OID 16405)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 5075 (class 0 OID 16491)
-- Dependencies: 229
-- Data for Name: enrollment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.enrollment (enrollment_id, student_id, grade_level, diagnostic_test_schedule, diagnostic_test_status, psa_status, sf10_status, goodmoral_status, enrollment_status, payment_scheme) FROM stdin;
1	1	K2	2026-05-15	f	t	t	t	1	QUARTERLY
2	3	N	2026-05-19	f	t	t	t	1	QUARTERLY
3	4	N	2026-05-14	f	f	f	f	1	QUARTERLY
4	5	N	2026-05-28	f	t	t	t	1	QUARTERLY
\.


--
-- TOC entry 5072 (class 0 OID 16458)
-- Dependencies: 226
-- Data for Name: guardian; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guardian (guardian_id, first_name, last_name, contact_number, address, occupation) FROM stdin;
1	First Name	LastName	12312312321	waewq	Occupation
2	First Name	LastName	121212	wewewqe	Occupation
3	First Name	LastName	Contact Number	ds	Occupation
4	First Name	LastName	12	sfss	Occupation
\.


--
-- TOC entry 5073 (class 0 OID 16472)
-- Dependencies: 227
-- Data for Name: guardian_of_student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guardian_of_student (guardian_id, student_id, relationship) FROM stdin;
1	1	Father
2	3	Father
3	4	Father
4	5	Father
\.


--
-- TOC entry 5077 (class 0 OID 16564)
-- Dependencies: 231
-- Data for Name: payment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.payment (payment_id, student_id, amount, payment_method, payment_plan, reference_number, payment_date) FROM stdin;
1	5	13550.00	Cash	ANNUAL	1283902189032013	2026-05-07
\.


--
-- TOC entry 5066 (class 0 OID 16391)
-- Dependencies: 220
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (role_id, role_name) FROM stdin;
1	Admin
2	Registrar
3	Teacher
\.


--
-- TOC entry 5070 (class 0 OID 16441)
-- Dependencies: 224
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student (student_id, grade_level, first_name, middle_name, last_name, address, age, birthdate, prev_school, has_allergies, medical_details) FROM stdin;
1	K2	First Name	Middle Name	Last Name	wew	5	2022-05-13	Previous School	t	sdsad
3	N	First Name	Middle Name	Last Name	weqe	9	2026-05-02	Previous School	f	
4	N	First Name	Middle Name	Last Name	sdds	4	2026-05-01	Previous School	f	
5	N	pol	Middle Name	Last Name	weqewq	6	2026-05-01	Previous School	f	
\.


--
-- TOC entry 5068 (class 0 OID 16402)
-- Dependencies: 222
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, role_id, name, password, email) FROM stdin;
1	1	System Administrator	$2a$12$n7Q9hBQIQr6gLrOujGc/l.SKfD3vZ0.zN8gk3XX07a3Myafobv5qC	admin@plla.com
2	3	Paul April	$2a$12$ENHMo9exr0Mcz4ukgWWRAukixE3Uw8PuEdackIq8YCqfch54nxU0m	paulaprilhofilena@gmail.com
3	2	Neil Sean	$2a$12$xAmb3ENHWErsUjARi3hYGuDsH16Smdkq/bl0h2mhJbowhP2RLnj32	neilsean@gmail.com
\.


--
-- TOC entry 5089 (class 0 OID 0)
-- Dependencies: 228
-- Name: enrollment_enrollment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.enrollment_enrollment_id_seq', 4, true);


--
-- TOC entry 5090 (class 0 OID 0)
-- Dependencies: 225
-- Name: guardian_guardian_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.guardian_guardian_id_seq', 4, true);


--
-- TOC entry 5091 (class 0 OID 0)
-- Dependencies: 230
-- Name: payment_payment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.payment_payment_id_seq', 1, true);


--
-- TOC entry 5092 (class 0 OID 0)
-- Dependencies: 219
-- Name: role_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_role_id_seq', 3, true);


--
-- TOC entry 5093 (class 0 OID 0)
-- Dependencies: 223
-- Name: student_student_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.student_student_id_seq', 5, true);


--
-- TOC entry 5094 (class 0 OID 0)
-- Dependencies: 221
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 3, true);


--
-- TOC entry 4910 (class 2606 OID 16505)
-- Name: enrollment enrollment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_pkey PRIMARY KEY (enrollment_id);


--
-- TOC entry 4908 (class 2606 OID 16479)
-- Name: guardian_of_student guardian_of_student_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guardian_of_student
    ADD CONSTRAINT guardian_of_student_pkey PRIMARY KEY (guardian_id, student_id);


--
-- TOC entry 4906 (class 2606 OID 16471)
-- Name: guardian guardian_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guardian
    ADD CONSTRAINT guardian_pkey PRIMARY KEY (guardian_id);


--
-- TOC entry 4912 (class 2606 OID 16581)
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (payment_id);


--
-- TOC entry 4896 (class 2606 OID 16398)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);


--
-- TOC entry 4898 (class 2606 OID 16400)
-- Name: role role_role_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_role_name_key UNIQUE (role_name);


--
-- TOC entry 4904 (class 2606 OID 16456)
-- Name: student student_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (student_id);


--
-- TOC entry 4900 (class 2606 OID 16416)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4902 (class 2606 OID 16414)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4916 (class 2606 OID 16506)
-- Name: enrollment fk_enrollment_student; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES public.student(student_id) ON DELETE CASCADE;


--
-- TOC entry 4914 (class 2606 OID 16480)
-- Name: guardian_of_student fk_gos_guardian; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guardian_of_student
    ADD CONSTRAINT fk_gos_guardian FOREIGN KEY (guardian_id) REFERENCES public.guardian(guardian_id) ON DELETE CASCADE;


--
-- TOC entry 4915 (class 2606 OID 16485)
-- Name: guardian_of_student fk_gos_student; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guardian_of_student
    ADD CONSTRAINT fk_gos_student FOREIGN KEY (student_id) REFERENCES public.student(student_id) ON DELETE CASCADE;


--
-- TOC entry 4917 (class 2606 OID 16582)
-- Name: payment fk_payment_student; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT fk_payment_student FOREIGN KEY (student_id) REFERENCES public.student(student_id) ON DELETE CASCADE;


--
-- TOC entry 4913 (class 2606 OID 16417)
-- Name: users fk_user_role; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES public.role(role_id);


-- Completed on 2026-05-09 23:48:46

--
-- PostgreSQL database dump complete
--

\unrestrict dgwpxy68qz7z59iIoP5ofRETF4QExhtg3evxOBrrJQevzzffqFzl0QFSx3NPLsd

