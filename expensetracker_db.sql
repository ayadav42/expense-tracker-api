-- because we may run this init more than once
drop user expense_tracker;
drop database expensetrackerdb;
drop table if exists users cascade;
drop table if exists categories cascade;
drop table if exists transactions cascade;

-- usually for applications we don't use sysadmin database users
--so we first create a user with a password
create user expense_tracker with password 'password';

-- and create a separate database as well for this user
create database expensetrackerdb with template=template0 owner=expense_tracker;

\connect expensetrackerdb;

-- need to grant privileges to the user
alter default privileges grant all on tables to expense_tracker;
alter default privileges grant all on sequences to expense_tracker;

create table users(
    id integer primary key not null,
    firstname varchar(20) not null,
    lastname varchar(20) not null,
    email varchar(30) not null,
    password text not null
);

create table categories(
    id integer primary key not null,
    user_id integer not null, -- one to one
    title varchar(20) not null,
    description varchar(50) not null
);

alter table categories add constraint categories_users_fk
foreign key (user_id) references users(id);

create table transactions(
    id integer primary key not null,
    category_id integer not null, --one to many
    user_id integer not null, --one to one
    amount numeric(10, 2) not null, -- type decimal with precision of 2
    note varchar(50) not null,
    transaction_date timestamp not null -- can use bigint as well
);

alter table transactions add constraint trans_cat_fk
foreign key (category_id) references categories(id);
alter table transactions add constraint trans_users_fk
foreign key (user_id) references users(id);

-- now we'll create sequences to manage auto-increments of primary key ids
create sequence if not exists users_seq increment 1 start 1;
create sequence if not exists categories_seq increment 1 start 1;
create sequence if not exists transactions_seq increment 1 start 1000;