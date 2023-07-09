CREATE TABLE  IF NOT EXISTS public.university_students
(
	id bigint NOT NULL,
	first_name character varying(100) COLLATE pg_catalog."default",
	last_name character varying(100) COLLATE pg_catalog."default",
	email character varying(100) COLLATE pg_catalog."default",
	CONSTRAINT university_students_pkey PRIMARY KEY (id)
);