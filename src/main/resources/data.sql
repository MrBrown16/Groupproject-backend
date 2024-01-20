insert into users (email,userName,firstName,lastName,phone,profileImagePath,deleted) values
('bbb@ccc.bu','moba','Ákos','Kovács',00303345677,'/photo/path/42',0),
('aaa@ddd.bu','Blob','Jóska','Kiss',1130322677,'/photo/path/43',0),
('ccc@eee.bu','Eevee','Mira','Nagy',4456345677,'/photo/path/45',0);

insert into organisations (name,logo_path,url,deleted) values
('Teenage Ninja Turtles', '/this/sick/logo','orgs/pages/2234',0),
('Make a Wish Fundation', '/this/sick/kid','orgs/pages/2235',0),
('Trash Monsters', '/dont/eat/trash','orgs/pages/2236',0);

insert into orgs_admins (org_id,user_id,deleted) values
((select org_id from organisations where name= 'Teenage Ninja Turtles'),(select user_id from users where email='bbb@ccc.bu'),0),
((select org_id from organisations where name= 'Make a Wish Fundation'),(select user_id from users where email='aaa@ddd.bu'),0),
((select org_id from organisations where name= 'Trash Monsters'),(select user_id from users where email='ccc@eee.bu'),0);

insert into posts (content,url,imagePath,likes,dislikes,user_id,org_id,deleted) values
('My magnificent content','/users/posts/1','profile/img/3',112,324,(select user_id from users where email='bbb@ccc.bu'),(select org_id from orgs_admins where user_id=1),0),--should be calculated via user_id, but don't want to add another select
('My magnificent content number 2','/users/posts/2','profile/img/3',152,3224,(select user_id from users where email='aaa@ddd.bu'),null,0),
('My magnificent content number 3','/users/posts/3','profile/img/3',132,3224,(select user_id from users where email='ccc@eee.bu'),null,0);

insert into votes (description,post_id,deleted) values
('These are your options',(SELECT post_id from posts WHERE content = 'My magnificent content'),0),
('These are your options 22',(SELECT post_id from posts WHERE content = 'My magnificent content number 2'),0),
('You can choose Between these',(SELECT post_id from posts WHERE content = 'My magnificent content number 3'),0);

update posts set vote_id=(select vote_id from votes where description='These are your options') where content='My magnificent content';
update posts set vote_id=(select vote_id from votes where description='These are your options 22') where content='My magnificent content number 2';
update posts set vote_id=(select vote_id from votes where description='You can choose Between these') where content='My magnificent content number 3';


insert into vote_options (optionText,votesNum,vote_id,deleted) values
('option A', 23, (select vote_id from votes where description='These are your options'),0),
('option B', 43, (select vote_id from votes where description='These are your options'),0),
('option C', 78, (select vote_id from votes where description='These are your options'),0),
('option 1', 1, (select vote_id from votes where description='These are your options 22'),0),
('option 2', 4, (select vote_id from votes where description='These are your options 22'),0),
('option 3', 444, (select vote_id from votes where description='These are your options 22'),0),
('option One', 444, (select vote_id from votes where description='You can choose Between these'),0),
('option Two', 404, (select vote_id from votes where description='You can choose Between these'),0),
('option None', 313, (select vote_id from votes where description='You can choose Between these'),0),
('option This', 500, (select vote_id from votes where description='You can choose Between these'),0);

insert into days (name) values
('Monday'),
('Tuesday'),
('Wednesday'),
('Thursday'),
('Friday'),
('Saturday'),
('Sunday');
select * from users
Inner join posts On posts.user_id=users.user_id;

select * from users;
select * from posts;
select * from organisations;
select * from votes;
select * from vote_options;