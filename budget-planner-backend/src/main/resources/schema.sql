create table user
(
    id bigint primary key,
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    username varchar(255)
);
create table role
(
    id bigint primary key,
    name varchar(255)
);

create table user_roles
(
    user_id  bigint not null references user,
    roles_id bigint not null references role,
    primary key (user_id, roles_id)
);

create table category
(
    id bigint primary key,
    name varchar(255)
);

create table budget
(
    id bigint primary key,
    amount double precision,
    date date,
    name varchar(255),
    type varchar(255),
    category_id bigint references category,
    user_id bigint references user
);

create table saving
(
    id bigint primary key,
    user_id bigint references user,
    month integer,
    year integer,
    total double precision
);

ALTER TABLE budget RENAME TO transaction;

ALTER TABLE "user" ADD status boolean;

ALTER TABLE transaction DROP COLUMN type;
ALTER TABLE transaction DROP COLUMN category_id;

CREATE TABLE transaction_type
(
    id bigint primary key,
    type varchar(255),
    category_id bigint references category
);

ALTER TABLE transaction ADD transaction_type_id bigint;
ALTER TABLE transaction ADD CONSTRAINT transaction_type FOREIGN KEY(transaction_type_id) REFERENCES transaction_type;

ALTER TABLE saving RENAME TO budget;

ALTER TABLE budget ADD balance double precision;
ALTER TABLE budget DROP COLUMN total;
ALTER TABLE budget ADD budget double precision;
ALTER TABLE budget ALTER COLUMN month TYPE varchar(255);

ALTER TABLE transaction ADD currency varchar(255);
ALTER TABLE budget ADD currency varchar(255);

create table notification
(
    id bigint primary key,
    date date,
    content varchar(255),
    read boolean,
    user_id bigint references "user"
);

create table notification
(
    id bigint primary key,
    date date,
    content varchar(255),
    read boolean,
    user_id bigint references "user"
);