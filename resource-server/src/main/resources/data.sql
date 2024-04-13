-- for reservations (not currently used)
-- INSERT INTO DAYS (DAY_ID, NAME) VALUES
-- (1,'Hétfő'),
-- (2,'Kedd'),
-- (3,'Szerda'),
-- (4,'Csütörtök'),
-- (5,'Péntek'),
-- (6,'Szombat'),
-- (7,'Vasárnap');

INSERT INTO ORGANISATIONS (ORG_ID,NAME,DELETED) VALUES
('1','FirstOrg',0),
('2','SecondOrg',0),
('3','ThirdOrg',0),
('4','OrgAdmins org',0),
('5','adminOrgAdmins org',0),
('6','just some org',0),
('7','Dr. Baka Károly: Kardiológus',0),
('8','Dr. Osbáth László: Háziorvos',0),
('9','Dr. Kun Béla: Sebész',0),
('10','Dr. Madár Katalin: Háziorvos',0);

INSERT INTO MYORG_CATEGORIES (DELETED, MYORG_ORG_ID, CATEGORIES) VALUES
(0,'7','DOCTOR'),
(0,'8','DOCTOR'),
(0,'9','DOCTOR'),
(0,'10','DOCTOR');

INSERT INTO users (USER_ID,EMAIL,FIRSTNAME,LASTNAME,PHONE,USERNAME,DELETED) VALUES
('1','bbb@ccc.bu','Ákos','Kovács',00303345677,'moba',0),
('2','aaa@ddd.bu','Jóska','Kiss',1130322677,'Blob',0),
('3','ccc@eee.bu','Mira','Nagy',4456345677,'Eevee',0),
('4','admin@email.bu','Admin','Admin',00303345677,'admin',0),
('5','user@email.bu','User','User',00303345677,'user',0),
('6','orgadmin@email.bu','OrgAdmin','OrgAdmin',00303345677,'orgadmin',0),
('7','adminorgadmin@email.bu','adminOrgAdmin','adminOrgAdmin',00303345677,'adminorgadmin',0);

INSERT INTO ITEMS ( ITEM_ID, CONDITION, CREATIONTIME, DESCRIPTION, LOCATION, NAME, PHONE, PRICE, UPDATETIME, DELETED, USER_ID ) VALUES
('1','new','2024-03-19 13:57:35.259333','Piros Bicikli','Budapest','bicikli',342391,0,'2024-03-19 13:57:35.259333',0,'1'),
('2','newish','2024-03-19 13:57:35.259333','Sárga Bicikli','Budapest','bicikli',342391,71000,'2024-03-19 13:57:35.259333',0,'2'),
('3','new','2024-03-19 13:57:35.259333','Éjjeli Szekrény','Budapest','Szekrény',342391,23099,'2024-03-19 13:57:35.259333',0,'3'),
('4','newish','2024-03-19 13:57:35.259333','Ágy','Budapest','Ágy',342391,10000,'2024-03-19 13:57:35.259333',0,'2'),
('5','used','2024-03-19 13:57:35.259333','Szinglik a környékeden','Budapest','Szinglik',342391213424,0,'2024-03-19 13:57:35.259333',0,'1');

INSERT INTO ORGS_ADMINS (USER_ID,ORG_ID,DELETED) VALUES
('4','1',0),--wouldn't be possible because user not ORG_ADMIN
('4','2',0),--wouldn't be possible because user not ORG_ADMIN
('4','3',0),--wouldn't be possible because user not ORG_ADMIN
('6','4',0),
('6','1',0),
('6','2',0),
('6','3',0),
('7','5',0),
('7','1',0),
('7','2',0),
('7','3',0);

-- INSERT INTO EVENTS (EVENT_ID, DESCRIPTION, ENDDATE, LOCATION, NAME, PUBLICEMAILS, PUBLICPHONES, STARTDATE, DELETED, ORG_ID, USER_ID) VALUES
-- ('1', 'First event ever! How exciting!!', ENDDATE, 'A planet called Earth', 'First ever event', 'PUBLICEMAILS', PUBLICPHONES, STARTDATE, DELETED, ORG_ID, USER_ID)

-- INSERT INTO NEWS (NEWS_ID, CONTENT, CREATIONTIME, TITLE, TYPE, UPDATETIME, DELETED, ORG_ID, USER_ID) VALUES

-- INSERT INTO NOTICES (NOTICE_ID, DATE, DESCRIPTION, LOCATION, PHONE, TYPE, URGENCY, DELETED, USER_ID) VALUES


-- INSERT INTO RESERVATIONS (RESERVATION_ID, EMAIL, ENDDATE, PHONE, PREFERREDNAME, STARTDATE, DELETED, ORG_ID, USER_ID) VALUES




-- INSERT INTO POSTS (POST_ID, CONTENT, CREATIONTIME, DISLIKES, LIKES, UPDATETIME, DELETED, ORG_ID, USER_ID, VOTE_ID) VALUES

-- INSERT INTO VOTES (VOTE_ID, DESCRIPTION, DELETED, POST_ID) VALUES

-- INSERT INTO VOTE_OPTIONS (VOTE_OPTION_ID, OPTIONTEXT, VOTESNUM, DELETED, VOTE_ID) VALUES

