drop table if exists account;
create table account(
    id int unsigned auto_increment,
    name varchar(40),
    password varchar(30),
    primary key (id)
);

insert into account(id, name, password) values(1, "aa", "dsd1");
insert into account(id, name, password) values(2, "ds", "1234");

set global time_zone='+8:00'

drop database storedev