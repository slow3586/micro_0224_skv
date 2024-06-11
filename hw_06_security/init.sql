CREATE TABLE IF NOT EXISTS users
(
    id         bigserial PRIMARY KEY,
    username   character varying(1000),
    password   character varying(1000),
    first_name character varying(1000),
    last_name  character varying(1000),
    email      character varying(1000),
    phone      character varying(1000)
);