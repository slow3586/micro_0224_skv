CREATE TABLE IF NOT EXISTS users
(
    id         bigserial PRIMARY KEY,
    username   character varying(1000) NOT NULL,
    password   character varying(1000) NOT NULL,
    first_name character varying(1000) NOT NULL,
    last_name  character varying(1000) NOT NULL,
    email      character varying(1000),
    phone      character varying(1000)
);