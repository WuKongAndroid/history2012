--Create Tables
create table book_category(
cid int primary key auto_increment,
cname varchar(32) not null,
ccontent varchar(50)
)
--timestamp���͵�ʱ��涨��ΧΪ 1970-01-01 00:00:01 ~ 2038 
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

--(���->����)�ֱ�Ϊ1->3  2->4   3->1   4->2    5->3   6->2
insert into book_category (cname,ccontent) values ('�Ƽ�','Android��������');
insert into book_category (cname,ccontent) values ('����','������Ů��');
insert into book_category (cname,ccontent) values ('����','������Թ��');
insert into book_category (cname,ccontent) values ('����','���߰��㴵');
insert into book_category (cname,ccontent) values ('ս��','����Ԯ��ս');
insert into book_category (cname,ccontent) values ('��ѧ','����ͼȫ��');

insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('4d3h6n8','�һ�����','��ֲ���֮����',true,'a/mym/zip','��ɷ�˹��','����˹','1980-03-25 13:24:26',3.63,0.4563,13,6);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('du56mh3','�����Ĳ���','ħ���񽣴��',false,'bcb/card/zip','������','�ͻ�˹̹','1993-05-17 06:04:58',2.03,0.5627,7,1);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('ku46hyt3','������','��������',false,'6kt/mm/dfgip','�Ա�����','�ձ�','1991-03-09 11:57:02',2.59,0.9863,42,2);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('8df2kvb','���μ�','��ɮ����˽�',true,'mnt/sdcard/zip','�޹���','�й�','1985-11-15',4.17,0.6746,12,6);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('m3k52h7','����ˮ��','�Բ���������',false,'5hyu/dyh/fdip','������','���ô�','2037-08-05 23:39:17',1.65,0.9763,9,5);


insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('8uhy7','���ߴ�','����������',false,'leo/yyq/rar','̨���½','�ϼ�����','1982-04-27 12:44:46',0.37,0.4863,4,4);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('4jk8uj','��Ƥ2','��ħ����',false,'fd/3fg/rt','������','Խ��','1993-05-17 06:04:58',3.12,0.4727,15,2);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('zloz98','ˮ䰴�','��ɽӢ��',true,'9hb/mtm/dc','��ݮ����','������','1989-04-29 16:37:32',4.44,0.5383,32,1);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('0pik7','�¸ҵ���','�Ǵ��̵���',false,'oky/ohuu/8ih','��˹����','�Ű�','1984-11-16',2.3,09.6966,85,5);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('uo3h2','����2����','�������Լ�',false,'8uj/5kg/khc','�����','�ɹ�','2026-02-25 13:49:57',2.39,0.9633,8,2);

insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('ku7d','��ף���','��ɽ����ףӢ̨',true,'3d/mkud/rdp','�Ŵ�һ����','����','1982-03-25 21:53:24',0.96,0.4563,17,3);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('4kl0','��ƿ��÷','�޵�������',false,'bt6/d6c/zgr','��������','�Ĵ�����','1997-05-17 06:04:52',2.25,0.5627,22,4);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('4kc3','��è����','ץ������',false,'3dh/ch6/drn','׿����','������','2006-02-22 21:47:53',4.89,0.9263,20,2);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('2d86','�����⴫','ѧϰ͵����',true,'rah/4rg/dj5','��������','���ɱ�','1979-06-22',3.66,0.6636,5,1);
insert into book_info (bookId,bookName,bookContent,bookIsOver,bookZipPath,bookWriter,countryName,updateTime,bookGradeNums,bookGradeRatio,bookGradeRater,cid)
values ('mu73','��«�޺ڻ�','�߸�������',true,'yk/55g/tj','һȺ���','ī����','2009-02-13 11:48:40',1.73,0.8003,19,5);

--Delete Tables
drop table book_category;
drop table book_info;
drop table user_info;