insert into users (USER_ID,EMAIL,FIRSTNAME,LASTNAME,PHONE,USERNAME,DELETED) values
('1','bbb@ccc.bu','Ákos','Kovács',00303345677,'moba',0),
('2','aaa@ddd.bu','Jóska','Kiss',1130322677,'Blob',0),
('3','ccc@eee.bu','Mira','Nagy',4456345677,'Eevee',0),
('4','admin@email.bu','Admin','Admin',00303345677,'admin',0),
('5','user@email.bu','User','User',00303345677,'user',0);


insert into ORGANISATIONS (ORG_ID,NAME,DELETED) values
('1','FirstOrg',0),
('2','SecondOrg',0),
('3','ThirdOrg',0);

insert into ORGS_ADMINS (USER_ID,ORG_ID,DELETED) values
('4','1',0),
('4','2',0),
('4','3',0),
('5','2',0),
('5','3',0);

