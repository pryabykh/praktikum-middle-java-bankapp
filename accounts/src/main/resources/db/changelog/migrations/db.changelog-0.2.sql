--liquibase formatted sql

--changeset intershop:1
create table accounts.notifications_outbox
(
    id    bigint primary key generated always as identity,
    login  text  not null,
    message  text  not null,
    received  boolean  not null default false
);
