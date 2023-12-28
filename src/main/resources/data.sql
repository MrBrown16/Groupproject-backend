insert into users (email,userName,firstName,lastName,phone,profile_image_path) values
('bbb@ccc.bu','moba','Ákos','Kovács',00303345677,'/photo/path/42'),
('aaa@ddd.bu','Blob','Jóska','Kiss',1130322677,'/photo/path/43'),
('ccc@eee.bu','Eeevee','Mira','Nagy',4456345677,'/photo/path/45');

insert into organisations (name,logo_path,url) values
('Teenage Ninja Turtles', '/this/sick/logo','orgs/pages/2234'),
('Make a Wish Fundation', '/this/sick/kid','orgs/pages/2235'),
('Trash Monsters', '/dont/eat/trash','orgs/pages/2236');

insert into orgs_admins (org_id,user_id) values
((select org_id from organisations where name= 'Teenage Ninja Turtles'),(select user_id from users where email='bbb@ccc.bu')),
((select org_id from organisations where name= 'Make a Wish Fundation'),(select user_id from users where email='aaa@ddd.bu')),
((select org_id from organisations where name= 'Trash Monsters'),(select user_id from users where email='ccc@eee.bu'));

insert into posts (content,url,imagePath,likes,dislikes,user_id) values
('My magnificent content','/users/posts/1','profile/img/3',112,324,(select user_id from users where email='bbb@ccc.bu')),
('My magnificent content number 2','/users/posts/2','profile/img/3',152,3224,(select user_id from users where email='aaa@ddd.bu')),
('My magnificent content number 3','/users/posts/3','profile/img/3',132,3224,(select user_id from users where email='ccc@eee.bu'));

insert into votes (description,post_id) values
('These are your options',(SELECT post_id from posts WHERE content = 'My magnificent content')),
('These are your options 22',(SELECT post_id from posts WHERE content = 'My magnificent content number 2')),
('You can choose Between these',(SELECT post_id from posts WHERE content = 'My magnificent content number 3'));

update posts set vote_id=(select vote_id from votes where description='These are your options') where content='My magnificent content';
update posts set vote_id=(select vote_id from votes where description='These are your options 22') where content='My magnificent content number 2';
update posts set vote_id=(select vote_id from votes where description='You can choose Between these') where content='My magnificent content number 3';


insert into vote_options (option_text,votes_num,vote_id) values
('option A', 23, (select vote_id from votes where description='These are your options')),
('option B', 43, (select vote_id from votes where description='These are your options')),
('option C', 78, (select vote_id from votes where description='These are your options')),
('option 1', 1, (select vote_id from votes where description='These are your options 22')),
('option 2', 4, (select vote_id from votes where description='These are your options 22')),
('option 3', 444, (select vote_id from votes where description='These are your options 22')),
('option One', 444, (select vote_id from votes where description='You can choose Between these')),
('option Two', 404, (select vote_id from votes where description='You can choose Between these')),
('option None', 313, (select vote_id from votes where description='You can choose Between these')),
('option This', 500, (select vote_id from votes where description='You can choose Between these'));


select * from users
Inner join posts On posts.user_id=users.user_id;

select * from users;
select * from posts;
select * from organisations;
select * from votes;
select * from vote_options;