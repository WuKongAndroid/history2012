--Create Tables
create table book_category(
cid int primary key auto_increment,
cname varchar(32) not null,
ccontent varchar(50)
)
--timestamp类型的时间规定范围为 1970-01-01 00:00:01 ~ 2038 
create table book_info(
bookId varchar (20) primary key,
bookName varchar(32) not null,
bookContent varchar(50),
bookIsOver boolean not null,
bookZipPath varchar(32) not null,
bookWriter varchar(32),
countryName varchar(20),
updateTime timestamp,
bookGradeNums float(3,2),
bookGradeRatio float(5,4),
bookGradeRater int,
cid int 
)
--Create User Table
create table user_info(
uid int primary key auto_increment,
uname varchar(32),
usex varchar(8),
ubirthday varchar(16),
ulocation varchar(32),
uemail varchar(32),
uhobby varchar(128),
uadvise varchar(128)
)

alter table book_info add constraint FK_cid foreign key (cid) references book_category (cid)

--(类别->数量)分别为1->3  2->4   3->1   4->2    5->3   6->2
insert into book_category (cname,ccontent) values ('科技','Android拯救世界');
insert into book_category (cname,ccontent) values ('言情','都市男女人');
insert into book_category (cname,ccontent) values ('武侠','江湖恩怨情');
insert into book_category (cname,ccontent) values ('玄幻','乱七八糟吹');
insert into book_category (cname,ccontent) values ('战争','抗美援朝战');
insert into book_category (cname,ccontent) values ('哲学','柏拉图全集');

insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('4d3h6n8','烈火金刚狼','金钢不败之神附体',true,'a/mym/zip','柴可夫斯基','俄罗斯','1980-03-25 13:24:26',3.63,0.4563,13,6);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('du56mh3','哈利的波特','魔法神剑打架',false,'bcb/card/zip','两条豺狗','巴基斯坦','1993-05-17 06:04:58',2.03,0.5627,7,1);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('ku46hyt3','海贼王','环游世界',false,'6kt/mm/dfgip','冈本杏奈','日本','1991-03-09 11:57:02',2.59,0.9863,42,2);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('8df2kvb','西游记','唐僧打猪八戒',true,'mnt/sdcard/zip','罗贯中','中国','1985-11-15',4.17,0.6746,12,6);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('m3k52h7','大力水手','吃菠菜有力量',false,'5hyu/dyh/fdip','阿道夫','加拿大','2037-08-05 23:39:17',1.65,0.9763,9,5);


insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('8uhy7','白蛇传','白素贞许仙',false,'leo/yyq/rar','台湾大陆','孟加拉国','1982-04-27 12:44:46',0.37,0.4863,4,4);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('4jk8uj','画皮2','妖魔打鬼怪',false,'fd/3fg/rt','幽灵王','越南','1993-05-17 06:04:58',3.12,0.4727,15,2);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('zloz98','水浒传','梁山英雄',true,'9hb/mtm/dc','蓝莓作者','利比亚','1989-04-29 16:37:32',4.44,0.5383,32,1);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('0pik7','勇敢的心','是打仗的人',false,'oky/ohuu/8ih','卡斯特罗','古巴','1984-11-16',2.3,09.6966,85,5);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('uo3h2','阿甘2正传','坚信是自己',false,'8uj/5kg/khc','五道口','蒙古','2026-02-25 13:49:57',2.39,0.9633,8,2);

insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('ku7d','梁祝天空','梁山伯打祝英台',true,'3d/mkud/rdp','古代一个人','巴西','1982-03-25 21:53:24',0.96,0.4563,17,3);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('4kl0','金瓶火梅','无敌西门庆',false,'bt6/d6c/zgr','靛青作者','澳大利亚','1997-05-17 06:04:52',2.25,0.5627,22,4);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('4kc3','黑猫警长','抓白老鼠',false,'3dh/ch6/drn','卓别林','北朝鲜','2006-02-22 21:47:53',4.89,0.9263,20,2);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('2d86','天书外传','学习偷天书',true,'rah/4rg/dj5','淘气的娃','菲律宾','1979-06-22',3.66,0.6636,5,1);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('mu73','葫芦娃黑画','七个白娃娃',true,'yk/55g/tj','一群蛤蟆','墨西哥','2009-02-13 11:48:40',1.73,0.8003,19,5);

--Delete Tables
drop table book_category;
drop table book_info;
drop table user_info;