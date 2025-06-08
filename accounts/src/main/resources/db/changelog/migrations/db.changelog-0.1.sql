--liquibase formatted sql

--changeset intershop:1
create table accounts.users
(
    id    bigint primary key generated always as identity,
    login  text  not null,
    password  text  not null,
    name  text  not null,
    birthdate  date  not null
);

create table accounts.accounts
(
    id    bigint primary key generated always as identity,
    currency  text  not null,
    balance  bigint  not null,
    user_id bigint references accounts.users (id),
    unique (currency, user_id)
);