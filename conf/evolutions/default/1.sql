# --- !Ups

CREATE TABLE users (
    id       bigserial    NOT NULL primary key,
    login    varchar(255) NOT NULL,
    password varchar(255) NOT NULL,

    CONSTRAINT uq_login UNIQUE(login)
);

# --- !Downs

drop table user;