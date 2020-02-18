-- !Ups

create table shared_model (
    "foo" varchar(255) primary key not null,
    "bar" integer not null
);

-- !Downs

drop table if exists shared_model;
