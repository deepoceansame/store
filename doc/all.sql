drop table if exists account;
create table account(
    id int unsigned auto_increment,
    name varchar(40),
    password varchar(30),
    primary key (id)
);

insert into account(id, name, password) values(1, "aa", "dsd1");
insert into account(id, name, password) values(2, "ds", "1234");
insert into account(id, name, password) values(3, "me", "7781");


drop table if exists goods;
create table goods(
  id int unsigned auto_increment,
  name varchar(50),
  description varchar(200),
  category_id int,
  img varchar(200),
  accountId int unsigned,
  price float,
  primary key (id),
  foreign key (accountId) references account(id)
);

insert into goods(name, description, accountId, category_id, price) value ('橘子', '非常好吃', 1, 2, 10);
insert into goods(name, description, accountId, category_id, price) value ('大橘子', '还可以', 1, 2, 8);
insert into goods(name, description, accountId, category_id, price) value ('耳机', '几乎全新', 2, 2, 30);
insert into goods(name, description, accountId, category_id, price) value ('旧耳机', '不新，但品质优秀', 2, 2, 30);
insert into goods(name, description, accountId, category_id, price) value ('陆战队员', '对空对地', 3, 4, 50);
insert into goods(name, description, accountId, category_id, price) value ('解放者', '对空aoe可架起高伤害对地', 3, 4, 300);
insert into goods(name, description, accountId, category_id, price) value ('雷神', '重型机甲', 3, 4, 1000);
insert into goods(name, description, accountId, category_id, price) value ('战列巡洋舰', '对空对地，可折跃', 3, 4, 1200);
insert into goods(name, description, accountId, category_id, price) value ('攻城坦克', '架起可造成高额aoe伤害', 3, 4, 700);
insert into goods(name, description, accountId, category_id, price) value ('女妖', '空中对地可隐形单位', 3, 4, 500);
insert into goods(name, description, accountId, category_id, price) value ('雷兽', '高血量高护甲', 2, 5, 1000);

