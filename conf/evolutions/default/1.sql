# --- !Ups

CREATE TABLE roles (
  id  serial PRIMARY KEY,
  name varchar(255) NOT NULL
);

CREATE TABLE users (
    id            bigserial    NOT NULL PRIMARY KEY,
    login         varchar(255) NOT NULL,
    password      varchar(255) NOT NULL,
    role_id       int NOT NULL REFERENCES roles(id),
    salt          int NOT NULL,

    connected     boolean NOT NULL DEFAULT false,
    last_activity timestamp,

    CONSTRAINT uq_login UNIQUE(login)
);

CREATE TABLE products (
  id    bigserial      NOT NULL PRIMARY KEY,
  name  varchar(255)   NOT NULL,
  price numeric(15, 2) NOT NULL
);


insert into roles(name) values ('employee');

insert into users(login, password, salt, role_id)
values ('admin', 'c674d9cdee160ebec3ed9ec138ac473480054483f185be25c27e51f35f30175f', 42, (select id from roles where name='employee'));

# --- !Downs

drop table users;

drop table roles;
