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

insert into ITEMS ( ITEM_ID, CONDITION, CREATIONTIME, DESCRIPTION, LOCATION, NAME, PHONE, PRICE, UPDATETIME, DELETED, USER_ID ) values
('1','new','2024-03-19 13:57:35.259333','Piros Bicikli','Budapest','bicikli',342391,0,'2024-03-19 13:57:35.259333',0,'1'),
('2','newish','2024-03-19 13:57:35.259333','Sárga Bicikli','Budapest','bicikli',342391,71000,'2024-03-19 13:57:35.259333',0,'2'),
('3','new','2024-03-19 13:57:35.259333','Éjjeli Szekrény','Budapest','Szekrény',342391,23099,'2024-03-19 13:57:35.259333',0,'3'),
('4','newish','2024-03-19 13:57:35.259333','Ágy','Budapest','Ágy',342391,10000,'2024-03-19 13:57:35.259333',0,'2'),
('5','used','2024-03-19 13:57:35.259333','Szinglik a környékeden','Budapest','Szinglik',342391213424,0,'2024-03-19 13:57:35.259333',0,'1');
