drop table if exists goods;
drop table if exists account cascade;
drop table if exists desiredgoods;
drop table if exists purchaseRecord;
drop table if exists message;
drop table if exists goodsimage;
drop table if exists messaged;
drop table if exists desiredgoodsimage;
drop table if exists supplyrecord;
drop table if exists purchaseRecord;

ALTER TABLE account
    ADD creditpoint int;

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
   foreign key (buyerId) references account(id) on delete cascade,
   foreign key (goodsId) references goods(id) on delete cascade
);

create table DesiredGoods(
     id          int unsigned auto_increment
         primary key,
     name        varchar(50)  not null ,
     description varchar(200) null,
     category_id int          null,
     img         varchar(200) null,
     account_id  int unsigned not null ,
     price       float        null,
     constraint goods_ibfk_2
         foreign key (account_id) references account (id)
             on delete cascade
);

create table message(
    senderid int unsigned,
    receiverid int unsigned,
    goodsid int unsigned,
    sendtime TIMESTAMP,
    type int,
    content varchar(200),
    foreign key (senderid) references account(id) on delete cascade,
    foreign key (receiverid) references account(id) on delete cascade,
    foreign key (goodsid) references goods(id) on delete cascade,
    primary key (senderid, receiverid, goodsid, sendtime)
);

create table messaged(
    senderid int unsigned,
    receiverid int unsigned,
    desiredgoodsid int unsigned,
    sendtime TIMESTAMP,
    type int,
    content varchar(200),
    foreign key (senderid) references account(id) on delete cascade,
    foreign key (receiverid) references account(id) on delete cascade,
    foreign key (desiredgoodsid) references desiredgoods(id) on delete cascade,
    primary key (senderid, receiverid, desiredgoodsid, sendtime)
);

create table goodsimage(
    goodsid int unsigned,
    img varchar(200),
    foreign key (goodsid) references goods(id) on delete cascade,
    primary key (goodsid, img)
);

create table desiredgoodsimage(
    desiredgoodsid int unsigned,
    img varchar(200),
    foreign key (desiredgoodsid) references desiredgoods(id) on delete cascade,
    primary key (desiredgoodsid, img)
);

create table supplyrecord(
    accountid int unsigned,
    desiredgoodsid int unsigned,
    primary key (accountid,desiredgoodsid),
    foreign key (accountid) references account(id) on delete cascade,
    foreign key (desiredgoodsid) references desiredgoods(id) on delete cascade
);


insert into account(id, name, password, mail, recvAddress) values(1, "aa", "dsd1", '3333@mail.com', '??????');
insert into account(id, name, password, mail, recvAddress) values(2, "ds", "1234", '4444@mail.com', '13???');
insert into account(id, name, password, mail, recvAddress) values(3, "me", "7781", '5555@mail.com', '?????????');

insert into goods(name, description, account_Id, category_id, price) value ('??????', '????????????', 1, 2, 10);
insert into goods(name, description, account_Id, category_id, price) value ('?????????', '?????????', 1, 2, 8);
insert into goods(name, description, account_Id, category_id, price) value ('??????', '????????????', 2, 2, 30);
insert into goods(name, description, account_Id, category_id, price) value ('?????????', '????????????????????????', 2, 2, 30);
insert into goods(name, description, account_Id, category_id, price) value ('????????????', '????????????', 3, 4, 50);
insert into goods(name, description, account_Id, category_id, price) value ('?????????', '??????aoe????????????????????????', 3, 4, 300);
insert into goods(name, description, account_Id, category_id, price) value ('??????', '????????????', 3, 4, 1000);
insert into goods(name, description, account_Id, category_id, price) value ('???????????????', '????????????????????????', 3, 4, 1200);
insert into goods(name, description, account_Id, category_id, price) value ('????????????', '?????????????????????aoe??????', 3, 4, 700);
insert into goods(name, description, account_Id, category_id, price) value ('??????', '???????????????????????????', 3, 4, 500);
insert into goods(name, description, account_Id, category_id, price) value ('??????', '??????????????????', 2, 5, 1000);

select * from desiredgoods where (description like concat('%', '???', '%'));
select concat('%','??????','%');


delete from GOODS where id = 29;

update account set money=IF(money is not null, null, money+10)

