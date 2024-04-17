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
('10','Dr. Madár Katalin: Háziorvos',0),
('11','Helyi Önkormányzat',0),
('12','Közművek',0);

INSERT INTO MYORG_CATEGORIES (DELETED, MYORG_ORG_ID, CATEGORIES) VALUES
(0,'7','DOCTOR'),
(0,'8','DOCTOR'),
(0,'9','DOCTOR'),
(0,'10','DOCTOR'),
(0,'11','GOV'),
(0,'12','UTILITY');

INSERT INTO users (USER_ID,EMAIL,FIRSTNAME,LASTNAME,PHONE,USERNAME,DELETED) VALUES
('1','bbb@ccc.bu','Ákos','Kovács',00303345677,'moba',0),
('2','aaa@ddd.bu','Jóska','Kiss',1130322677,'Blob',0),
('3','ccc@eee.bu','Mira','Nagy',4456345677,'Eevee',0),
('4','admin@email.bu','Admin','Admin',00303345677,'admin',0),
('5','user@email.bu','User','User',00303345677,'user',0),
('6','orgadmin@email.bu','OrgAdmin','OrgAdmin',00303345677,'orgadmin',0),
('7','adminorgadmin@email.bu','adminOrgAdmin','adminOrgAdmin',00303345677,'adminorgadmin',0);

INSERT INTO ITEMS ( ITEM_ID, CONDITION, CREATIONTIME, DESCRIPTION, LOCATION, NAME, PHONE,EMAIL, PRICE, UPDATETIME, DELETED, USER_ID ) VALUES
('1','new','2024-03-19 13:57:35.259333','Piros Bicikli','Budapest','bicikli',342391,'email1@mail.bu',0,'2024-03-19 13:57:35.259333',0,'1'),
('2','newish','2024-03-19 13:57:35.259333','Sárga Bicikli','Budapest','bicikli',342391,'email2@mail.bu',71000,'2024-03-19 13:57:35.259333',0,'2'),
('3','new','2024-03-19 13:57:35.259333','Éjjeli Szekrény','Budapest','Szekrény',342391,'email3@mail.bu',23099,'2024-03-19 13:57:35.259333',0,'3'),
('4','newish','2024-03-19 13:57:35.259333','Ágy','Budapest','Ágy',342391,'email4@mail.bu',10000,'2024-03-19 13:57:35.259333',0,'4'),
('5','used','2024-03-19 13:57:35.259333','Szinglik a környékeden','Budapest','Szinglik',342391213424,'email5@mail.bu',0,'2024-03-19 13:57:35.259333',0,'5'),
('6','new','2024-04-14 00:00:00','Eladó fürjek, ketreccel vihetőek','Sümeg','Eladó fürjek, ketreccel vihetőek',564764654,'47658',1398535,'2024-04-14 00:00:00',0,'6'),
('7','used','2024-04-14 00:00:00','Samsung hangszóró eladó','8554','Samsung hangszóró eladó',46543,'46467',456456,'2024-04-14 00:00:00',0,'7'),
('8','new','2024-04-14 00:00:00','Éjjeli Szekrény','Budapest','Szekrény',342391,'email3@mail.bu',23099,'2024-04-14 00:00:00',0,'1'),
('9','newish','2024-04-14 00:00:00','Ágy','Budapest','Ágy',342391,'email4@mail.bu',10000,'2024-04-14 00:00:00',0,'2'),
('10','newish','2024-04-14 00:00:00','Sárga Bicikli','Budapest','bicikli',342391,'email2@mail.bu',71000,'2024-04-14 00:00:00',0,'3'),
('11','new','2024-04-14 00:00:00','Samsung TV adó el','8554','Samsung TV adó el',46543,'4646567',456456,'2024-04-14 00:00:00',0,'4'),
('12','new','2024-04-14 00:00:00','IV. Baldwin király tanításai vihetőek','8554','IV. Baldwin király tanításai vihetőek',46543,'4646567',456456,'2024-04-14 00:00:00',0,'5'),
('13','new','2024-04-14 00:00:00','IV. Baldwin király tanításai vihetőek','8554','IV. Baldwin király tanításai vihetőek',46543,'4646567',456456,'2024-04-14 00:00:00',0,'6'),
('14','used','2024-04-14 00:00:00','Sérült szekrény elvihető','Budapest','Sérült szekrény elvihető',564764654,'47658',13535,'2024-04-14 00:00:00',0,'7'),
('15','new','2024-04-14 00:00:00','LG Monitor eladó','8554','LG Monitor eladó',46543,'4646567',456456,'2024-04-14 00:00:00',0,'1'),
('16','newish','2024-04-14 00:00:00','Alig használt éjjeli lámpa','Pócsag','Alig használt éjjeli lámpa',243234521,'12314345',343215,'2024-04-14 00:00:00',0,'2');


INSERT INTO ORGS_ADMINS (USER_ID,ORG_ID,DELETED) VALUES
-- ('4','1',0),--wouldn't be possible because user not ORG_ADMIN
-- ('4','2',0),--wouldn't be possible because user not ORG_ADMIN
-- ('4','3',0),--wouldn't be possible because user not ORG_ADMIN
('6','4',0),
('6','1',0),
('6','2',0),
('6','3',0),
('7','5',0),
('7','1',0),
('7','2',0),
('7','3',0);

INSERT INTO EVENTS (EVENT_ID, DESCRIPTION, ENDDATE, LOCATION, NAME, PUBLICEMAILS, PUBLICPHONES, STARTDATE, DELETED, ORG_ID, USER_ID) VALUES
('977fdc7b-9d58-48ec-aa33-d376e03c4144', 'First event ever! How exciting!!', TIMESTAMP '2024-04-17 14:59:59.387793', 'A planet called Earth', 'First ever event', ARRAY ['PUBLICEMAIL1@email.bu', 'PUBLICEMAIL2@email.bu', 'PUBLICEMAIL2@email.bu'], ARRAY [12345678901, 12345678902, 12345678903], TIMESTAMP '2024-04-17 14:59:59.387793', FALSE, '5', '7'),
('e8374269-76da-4610-9e75-0fc12c3f015a', 'Second is not so bad...', TIMESTAMP '2024-04-17 14:59:59.41479', 'A planet called Mars', 'Not the First ever event', ARRAY ['PUBLICEMAIL12@email.bu', 'PUBLICEMAIL22@email.bu', 'PUBLICEMAIL22@email.bu'], ARRAY [12345433901, 12345458902, 12345656903], TIMESTAMP '2024-04-17 14:59:59.41479', FALSE, '4', '6'),
('a764b4ad-d74b-4ad3-aa1b-6c1e485a301a', 'Ohh we are only third? nevermind will do it', TIMESTAMP '2024-04-17 14:59:59.422785', 'A planet called Venus', 'First ever event! What you say we aren''t??', ARRAY ['PUBLICEMAIL13@email.bu', 'PUBLICEMAIL23@email.bu', 'PUBLICEMAIL23@email.bu'], ARRAY [12343223901, 12343223902, 12343223903], TIMESTAMP '2024-04-17 14:59:59.422785', FALSE, '3', '5'),
('7e32c349-02a9-48ba-b0de-000715fc4f36', 'First event ever! How exciting!!', TIMESTAMP '2024-04-17 15:17:52.78386', 'A planet called Earth', 'First ever event', ARRAY ['PUBLICEMAIL1@email.bu', 'PUBLICEMAIL2@email.bu', 'PUBLICEMAIL2@email.bu'], ARRAY [12345678901, 12345678902, 12345678903], TIMESTAMP '2024-04-17 15:17:52.78386', FALSE, '5', '7'),
('0f0c984c-07ec-4f54-80a9-eb6a6395d913', 'Second is not so bad...', TIMESTAMP '2024-04-17 15:17:52.788864', 'A planet called Mars', 'Not the First ever event', ARRAY ['PUBLICEMAIL12@email.bu', 'PUBLICEMAIL22@email.bu', 'PUBLICEMAIL22@email.bu'], ARRAY [12345433901, 12345458902, 12345656903], TIMESTAMP '2024-04-17 15:17:52.788864', FALSE, '4', '6'),
('1acf4b0e-6db4-4cfc-963c-c6d5150a009b', 'Ohh we are only third? nevermind will do it', TIMESTAMP '2024-04-17 15:17:52.793861', 'A planet called Venus', 'First ever event! What you say we aren''t??', ARRAY ['PUBLICEMAIL13@email.bu', 'PUBLICEMAIL23@email.bu', 'PUBLICEMAIL23@email.bu'], ARRAY [12343223901, 12343223902, 12343223903], TIMESTAMP '2024-04-17 15:17:52.793861', FALSE, '3', '5');

INSERT INTO NEWS (NEWS_ID, CONTENT, CREATIONTIME, TITLE, TYPE, UPDATETIME, DELETED, ORG_ID, USER_ID) VALUES
('32503cbb-a74f-46a0-b2be-018abae4f699', U&'Alap\00edtv\00e1nyi t\00e1mogat\00e1ssal vitte szakmai kir\00e1ndul\00e1sokra 11-12. \00e9vfolyamos di\00e1kjait a K\00f6zg\00e9.', TIMESTAMP '2024-04-17 14:59:59.462755', U&'(orsz\00e1gos) Szakmai kir\00e1ndul\00e1sok a Nemzeti Egy\00fcttm\0171k\00f6d\00e9si Alap t\00e1mogat\00e1s\00e1val', 'NATIONAL', TIMESTAMP '2024-04-17 14:59:59.462755', FALSE, '5', '7'),
('5ad2d14d-ff3a-4d2d-ac83-3b249996e84b', U&'Kis-Fekete Vilmos, a Kiskunf\00e9legyh\00e1zi Bal\00e1zs \00c1rp\00e1d Alapfok\00fa M\0171v\00e9szeti Iskola int\00e9zm\00e9nyvezet\0151je tartott el\0151ad\00e1st m\00e1rcius 5-\00e9n, kedden a v\00e1rosi k\00f6nyvt\00e1r baba-mama klubj\00e1ban. A Bet\0171b\00f6lcsiben ez\00fattal a korai hang\00e9lm\00e9nyekr\0151l \00e9s a zenei nevel\00e9s fontoss\00e1g\00e1r\00f3l hallhattak az \00e9rdekl\0151d\0151k.', TIMESTAMP '2024-04-17 14:59:59.464754', U&'(orsz\00e1gos) Zenei \00e9lm\00e9nyek a Bet\0171b\00f6lcsiben', 'NATIONAL', TIMESTAMP '2024-04-17 14:59:59.464754', FALSE, '3', '7'),
('97c62c85-a109-4792-83b6-3a0d77d1569b', U&'M\00e1rcius els\0151 h\00e9tv\00e9g\00e9j\00e9n rendezt\00e9k meg Kiskunf\00e9legyh\00e1z\00e1n a XI. Csik\00f3s J\00f3zsef eml\00e9ktorn\00e1t. A sok versenyz\0151 csapatnak k\00f6sz\00f6nhet\0151en 2 helysz\00ednen zajlottak a teremlabdar\00fag\00f3-m\00e9rk\0151z\00e9sek. A F\00e9legyh\00e1zi T\00e9rs\00e9gi Sportiskola U11 koroszt\00e1lya 3 csapatot ind\00edtott a megm\00e9rettet\00e9sen. Az FTSI \201eA\201d \00e9s FTSI \201eB\201d csapat a Kiskunf\00e9legyh\00e1zi V\00e1rosi Sportcsarnokban, a harmadik csapat a Mezg\00e9 sportcsarnok\00e1ban ontotta a g\00f3lokat.', TIMESTAMP '2024-04-17 14:59:59.466755', U&'(orsz\00e1gos) \00c9remes\0151 a XI. Csik\00f3s J\00f3zsef eml\00e9ktorn\00e1n', 'NATIONAL', TIMESTAMP '2024-04-17 14:59:59.466755', FALSE, '1', '4'),
('355c6e5f-2686-40da-962d-d41a56e55de6', 'What could i write thats worthy to be the first? Im not worthy', TIMESTAMP '2024-04-17 15:17:52.79686', 'First ever article!!', 'INTERNATIONAL', TIMESTAMP '2024-04-17 15:17:52.79686', FALSE, '4', '6'),
('a94f53cf-570c-47e8-ad0e-ee41df61bd06', 'What could i write thats worthy to be the first? Im not worthy', TIMESTAMP '2024-04-17 15:17:52.799588', 'First ever article!!', 'INTERNATIONAL', TIMESTAMP '2024-04-17 15:17:52.799588', FALSE, '5', '7'),
('01c81fc9-8033-4fb8-b3cf-37139678597f', 'What could i write thats worthy to be the first? Im not worthy', TIMESTAMP '2024-04-17 15:17:52.802109', 'First ever article!!', 'INTERNATIONAL', TIMESTAMP '2024-04-17 15:17:52.802109', FALSE, '3', '4'),
('3111e1be-df42-434f-beee-4dd7b177740b', 'What could i write thats worthy to be the first? Im not worthy', TIMESTAMP '2024-04-17 15:17:52.808111', 'First ever article!!', 'INTERNATIONAL', TIMESTAMP '2024-04-17 15:17:52.808111', FALSE, '2', '4'),
('c5de8e2d-b7c9-4079-a467-af1c7d2342bb', 'What could i write thats worthy to be the first? Im not worthy', TIMESTAMP '2024-04-17 15:17:52.809108', 'First ever article!!', 'INTERNATIONAL', TIMESTAMP '2024-04-17 15:17:52.809108', FALSE, '3', '6'),
('f6155fe3-1407-4781-b74b-14c422b8b0da', U&'Ism\00e9t \00f6ssze\00fclt a K\00f6nyvek klubja olvas\00f3k\00f6r a Kiskunf\00e9legyh\00e1zi Pet\0151fi S\00e1ndor V\00e1rosi K\00f6nyvt\00e1rban dr. Szab\00f3 Imre filoz\00f3fus vezet\00e9s\00e9vel. A m\00e1rcius 5-ei esem\00e9nyen Oliver Sacks: A f\00e9rfi, aki kalapnak n\00e9zte a feles\00e9g\00e9t c\00edm\0171 m\0171v\00e9t elemezt\00e9k.', TIMESTAMP '2024-04-17 15:17:52.811108', U&'Ism\00e9t \00f6ssze\00fclt a K\00f6nyvek klubja olvas\00f3k\00f6r a k\00f6nyvt\00e1rban', 'LOCAL', TIMESTAMP '2024-04-17 15:17:52.811108', FALSE, '3', '6'),
('c604541c-fcbd-429a-85f2-03e5b264abce', U&'Alap\00edtv\00e1nyi t\00e1mogat\00e1ssal vitte szakmai kir\00e1ndul\00e1sokra 11-12. \00e9vfolyamos di\00e1kjait a K\00f6zg\00e9.', TIMESTAMP '2024-04-17 15:17:52.813111', U&'Szakmai kir\00e1ndul\00e1sok a Nemzeti Egy\00fcttm\0171k\00f6d\00e9si Alap t\00e1mogat\00e1s\00e1val', 'LOCAL', TIMESTAMP '2024-04-17 15:17:52.813111', FALSE, '5', '7'); 

INSERT INTO NOTICES (NOTICE_ID, DATE, DESCRIPTION, LOCATION, PHONE, TYPE, URGENCY, DELETED, USER_ID) VALUES
('ec8deb00-ec53-4076-bb67-ca3402f9d2c2', TIMESTAMP '2024-04-17 01:20:00', 'hip', '543tg', 21234, 'KOZTERULET', '1', FALSE, '1'),
('3926e198-36a1-4457-aa42-48af87f1c7c6', TIMESTAMP '2024-04-17 01:20:00', 'hip', '543tg', 21234, 'KOZTERULET', '1', FALSE, '2'),
('ef0c4408-7f6a-45fc-8bee-0974261b431c', TIMESTAMP '2024-04-19 01:20:00', 'hip', '543tg', 21234, 'UTHIBA', '2', FALSE, '3'),
('7832d779-cc26-4215-b9de-427c6f6e65a0', TIMESTAMP '2024-04-19 01:20:00', 'hip', '543tg', 21234, 'UTHIBA', '2', FALSE, '4'),
('df6cd820-165e-4078-a556-bb9f055465b0', TIMESTAMP '2024-04-20 01:20:00', 'hip', '543tg', 21234, 'VIZGAZ', '3', FALSE, '5'),
('6b5faca3-62ed-4a2e-8dcd-fb0d898b04d4', TIMESTAMP '2024-04-20 01:20:00', 'hip', '543tg', 21234, 'VIZGAZ', '3', FALSE, '6'),
('24b2b610-6f38-4663-9f68-9b00ed343cbc', TIMESTAMP '2024-04-21 01:20:00', 'hip', '543tg', 21234, 'LOMTALANITAS', '4', FALSE, '7'),
('70a3cd9d-b54e-4b78-b2df-34cbae9f3b37', TIMESTAMP '2024-04-21 01:20:00', 'hip', '543tg', 21234, 'LOMTALANITAS', '4', FALSE, '1'),
('7fbe1dcb-0da6-4d7c-8d66-233212e8bda8', TIMESTAMP '2024-04-22 01:20:00', 'hip', '543tg', 21234, 'SZEMETSZALLITAS', '1', FALSE, '2'),
('0064a7cc-a2be-451d-ad9a-91f99fa72bec', TIMESTAMP '2024-04-22 01:20:00', 'hip', '543tg', 21234, 'SZEMETSZALLITAS', '1', FALSE, '3'),
('a3778b3d-9e1b-4295-99f6-5e193ed02ede', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'KOZTERULET', '2', FALSE, '4'),
('687ecc78-2bae-431c-90a3-184e781ee833', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'KOZTERULET', '2', FALSE, '5'),
('6544864b-0623-4631-a53f-b794828b9c03', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'UTHIBA', '2', FALSE, '6'),
('351604e3-3dbe-4870-a93c-e5a5d70b48db', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'UTHIBA', '2', FALSE, '7'),
('e5095f66-be50-44fd-8b28-0c3b238bc6d1', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'VIZGAZ', '2', FALSE, '1'),
('33e178d8-139a-4fde-9cf0-9508877954e8', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'VIZGAZ', '2', FALSE, '2'),
('d5921ff8-78c9-475f-90f9-5fc538d74c94', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'LOMTALANITAS', '2', FALSE, '3'),
('0d9cd84a-aac2-4d54-951b-ab6009457d38', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'LOMTALANITAS', '2', FALSE, '4'),
('a8acd980-2cf3-4250-9c02-22f59bd9fa70', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'SZEMETSZALLITAS', '2', FALSE, '5'),
('e855252b-59cd-4cf7-bf50-598c77607afb', TIMESTAMP '2024-04-23 01:20:00', 'hip', '543tg', 21234, 'SZEMETSZALLITAS', '2', FALSE, '6'),
('28b237fb-cf3e-4494-ac92-4243ac804af9', TIMESTAMP '2024-04-17 14:59:59.467755', 'There is an alien SpaceShip parked in front of our house illegaly', 'our address', 12345678901, 'KOZTERULET', '3', FALSE, '5'),
('0060ee84-91a6-4d8d-a40f-44937d82db5a', TIMESTAMP '2024-04-17 14:59:59.474756', 'There is an alien SpaceShip parked in front of our house illegaly', 'our address', 12345678901, 'KOZTERULET', '3', FALSE, '2'),
('c7ffcffc-e93f-4b09-b5ac-7eb4fae7be0c', TIMESTAMP '2024-04-17 14:59:59.478759', 'There is an alien SpaceShip parked in front of our house illegaly', 'our address', 12345678901, 'KOZTERULET', '2', FALSE, '3'),
('16eeeadd-9be0-474f-8f66-c08f5fb03960', TIMESTAMP '2024-04-17 15:17:52.833928', 'There is an alien SpaceShip parked in front of our house illegaly', 'our address', 12345678901, 'KOZTERULET', '3', FALSE, '5'),
('95d9ba5a-b94a-4412-9570-d0abf96846f6', TIMESTAMP '2024-04-17 15:17:52.838948', 'There is an alien SpaceShip parked in front of our house illegaly', 'our address', 12345678901, 'KOZTERULET', '3', FALSE, '2'),
('498c2f92-0dd3-4bd3-b731-c68718ddaf0b', TIMESTAMP '2024-04-17 15:17:52.84293', 'There is an alien SpaceShip parked in front of our house illegaly', 'our address', 12345678901, 'KOZTERULET', '2', FALSE, '3'); 

INSERT INTO RESERVATIONS (RESERVATION_ID, EMAIL, ENDDATE, PHONE, PREFERREDNAME, STARTDATE, DELETED, ORG_ID, USER_ID) VALUES
('6bb860f6-af88-4332-9349-e65a7469292a', 'billspublicemail@email.bu', TIMESTAMP '2024-04-17 14:59:59.481754', 123456780, 'Bill', TIMESTAMP '2024-04-17 14:59:59.481754', FALSE, '6', '1'),
('b1bda6f7-cb22-4cf1-8ad4-b6613bfbdf85', 'marispublicemail@email.bu', TIMESTAMP '2024-04-17 14:59:59.489755', 123456780, 'Mari', TIMESTAMP '2024-04-17 14:59:59.489755', FALSE, '5', '3'),
('75e11df6-6d44-46b0-a6ad-c1caea43c556', 'balintspublicemail@email.bu', TIMESTAMP '2024-04-17 14:59:59.492241', 123456780, U&'B\00e1lint', TIMESTAMP '2024-04-17 14:59:59.492241', FALSE, '4', '2'),
('243c27f1-1c26-486b-8365-b9f53cfe018e', 'billspublicemail@email.bu', TIMESTAMP '2024-04-17 15:17:52.846993', 123456780, 'Bill', TIMESTAMP '2024-04-17 15:17:52.846993', FALSE, '6', '1'),
('46445a9c-a5e3-4c79-8ccd-136c64c4a9fe', 'marispublicemail@email.bu', TIMESTAMP '2024-04-17 15:17:52.851994', 123456780, 'Mari', TIMESTAMP '2024-04-17 15:17:52.851994', FALSE, '5', '3'),
('1f9796c6-e78e-4e25-9226-5cbacf46f8b1', 'balintspublicemail@email.bu', TIMESTAMP '2024-04-17 15:17:52.856998', 123456780, U&'B\00e1lint', TIMESTAMP '2024-04-17 15:17:52.856998', FALSE, '4', '2');

-- INSERT INTO POSTS (POST_ID, CONTENT, CREATIONTIME, DISLIKES, LIKES, UPDATETIME, DELETED, ORG_ID, USER_ID, VOTE_ID) VALUES

-- INSERT INTO VOTES (VOTE_ID, DESCRIPTION, DELETED, POST_ID) VALUES

-- INSERT INTO VOTE_OPTIONS (VOTE_OPTION_ID, OPTIONTEXT, VOTESNUM, DELETED, VOTE_ID) VALUES

