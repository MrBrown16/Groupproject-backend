
create table users(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(150) not null,
	enabled boolean not null
);
create table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);


-- starting point to the extended roles and authorities "TODOS"
-- create table users(
-- 	user_id BIGINT unsigned not null primary key,
-- 	username varchar_ignorecase(50) not null unique,
-- 	password varchar_ignorecase(50) not null,
-- 	enabled boolean not null default true,
-- 	deleted boolean not null default false
-- );
-- create table clients(
-- 	client_id varchar_ignorecase(50) not null primary key,
-- 	client_name varchar_ignorecase(50) not null unique,
-- 	enabled boolean not null default true,
-- 	deleted boolean not null default false
-- );

-- create table clients_users (
-- 	clients_users_id BIGINT unsigned not null primary key
-- 	user_id varchar_ignorecase(50) not null,
-- 	client_id varchar_ignorecase(50) not null,
-- 	constraint fk_clients_users foreign key(user_id) references users(user_id)
-- 	constraint fk_users_clients foreign key(client_id) references clients(client_id)
-- );
-- create table clients_users_authorities (
-- 	clients_users_id BIGINT unsigned not null,
-- 	authority_id integer not null,
-- 	constraint fk_clients_users_authorities foreign key(clients_users_id) references clients_users(clients_users_id)
-- 	constraint fk_users_clients_authorities foreign key(authority_id) references authorities(authority_id)
-- );

-- create table authorities (
-- 	authority_id integer not null primary key,
-- 	authority varchar_ignorecase(50) not null
-- );
-- create unique index ix_auth_username on authorities (username,authority);
