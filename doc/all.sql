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


drop table if exists goods;
create table goods(
  id int unsigned auto_increment,
  name varchar(50),
  description varchar(200),
  category_id int,
  img varchar(200),
  accountId int unsigned,
  primary key (id),
  foreign key (accountId) references account(id)
);

insert into goods(name, description, accountId) value ('橘子', '非常好吃', 1);
insert into goods(name, description, accountId) value ('大橘子', '还可以', 1);
insert into goods(name, description, accountId) value ('耳机', '几乎全新', 2);
insert into goods(name, description, accountId) value ('旧耳机', '不新，但品质优秀', 2);