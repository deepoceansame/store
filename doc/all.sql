drop table if exists account;
create table account(
    id int unsigned auto_increment,
    name varchar(40),
    password varchar(30),
    primary key (id)
)