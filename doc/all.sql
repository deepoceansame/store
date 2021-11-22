drop table if exists goods;
drop table if exists account cascade;


create table account(
    id int unsigned auto_increment,
    name varchar(40),
    password varchar(200),
    mail varchar(70) unique,
    recvAddress varchar(100),
    avator varchar(100),
    qq varchar(30),
    money float,
    primary key (id)
);

create table goods(
  id int unsigned auto_increment,
  name varchar(50),
  description varchar(200),
  category_id int,
  img varchar(200),
  account_id int unsigned,
  price float,
  primary key (id),
  foreign key (account_id) references account(id) on delete cascade
);

create table purchaseRecord(
   buyerId int(10) unsigned ,
   goodsId int(10) unsigned,
   effective varchar(5) check ( effective in ('true','false')),
   primary key (buyerId,goodsId),
   foreign key (buyerId) references account(id),
   foreign key (goodsId) references goods(id)
);

insert into account(id, name, password, mail, recvAddress) values(1, "aa", "dsd1", '3333@mail.com', '荔园');
insert into account(id, name, password, mail, recvAddress) values(2, "ds", "1234", '4444@mail.com', '13栋');
insert into account(id, name, password, mail, recvAddress) values(3, "me", "7781", '5555@mail.com', '图书馆');

insert into goods(name, description, account_Id, category_id, price) value ('橘子', '非常好吃', 1, 2, 10);
insert into goods(name, description, account_Id, category_id, price) value ('大橘子', '还可以', 1, 2, 8);
insert into goods(name, description, account_Id, category_id, price) value ('耳机', '几乎全新', 2, 2, 30);
insert into goods(name, description, account_Id, category_id, price) value ('旧耳机', '不新，但品质优秀', 2, 2, 30);
insert into goods(name, description, account_Id, category_id, price) value ('陆战队员', '对空对地', 3, 4, 50);
insert into goods(name, description, account_Id, category_id, price) value ('解放者', '对空aoe可架起高伤害对地', 3, 4, 300);
insert into goods(name, description, account_Id, category_id, price) value ('雷神', '重型机甲', 3, 4, 1000);
insert into goods(name, description, account_Id, category_id, price) value ('战列巡洋舰', '对空对地，可折跃', 3, 4, 1200);
insert into goods(name, description, account_Id, category_id, price) value ('攻城坦克', '架起可造成高额aoe伤害', 3, 4, 700);
insert into goods(name, description, account_Id, category_id, price) value ('女妖', '空中对地可隐形单位', 3, 4, 500);
insert into goods(name, description, account_Id, category_id, price) value ('雷兽', '高血量高护甲', 2, 5, 1000);

