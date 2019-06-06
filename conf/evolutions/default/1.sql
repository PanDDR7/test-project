# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table backend_user (
  id                            integer auto_increment not null,
  user_id                       varchar(255),
  user_name                     varchar(255),
  user_gender                   varchar(255),
  user_uuid                     varchar(255),
  user_account                  varchar(255),
  user_password                 varchar(255),
  create_datetime               datetime(6) not null,
  modify_datetime               datetime(6) not null,
  constraint pk_backend_user primary key (id)
);

create table front_user (
  id                            integer auto_increment not null,
  user_id                       varchar(255),
  user_name                     varchar(255),
  user_gender                   varchar(255),
  user_uuid                     varchar(255),
  user_account                  varchar(255),
  user_password                 varchar(255),
  create_datetime               datetime(6) not null,
  modify_datetime               datetime(6) not null,
  constraint pk_front_user primary key (id)
);

create table product (
  id                            integer auto_increment not null,
  name                          varchar(255),
  price                         integer not null,
  account                       varchar(255),
  password                      varchar(255),
  create_datetime               datetime(6) not null,
  modify_datetime               datetime(6) not null,
  constraint pk_product primary key (id)
);

create table shopping_cart (
  id                            integer auto_increment not null,
  user_id                       varchar(255),
  product_id                    integer not null,
  quantity                      varchar(255),
  create_datetime               datetime(6) not null,
  constraint pk_shopping_cart primary key (id)
);


# --- !Downs

drop table if exists backend_user;

drop table if exists front_user;

drop table if exists product;

drop table if exists shopping_cart;

