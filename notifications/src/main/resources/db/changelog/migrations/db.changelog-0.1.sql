--liquibase formatted sql

--changeset intershop:1
create table notifications.notifications
(
    id    bigint primary key generated always as identity,
    source_id bigint not null unique,
    login  text  not null,
    message  text  not null
);