# --- !Ups

CREATE TABLE users (
    id            bigserial    NOT NULL primary key,
    login         varchar(255) NOT NULL,
    password      varchar(255) NOT NULL,

    connected     boolean NOT NULL DEFAULT false,
    last_activity timestamp,

    CONSTRAINT uq_login UNIQUE(login)
);

insert into users(login, password) values ('admin', 'admin');

# --- !Downs

drop table users;
