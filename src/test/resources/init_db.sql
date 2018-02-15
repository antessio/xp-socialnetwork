create table user(
	username varchar(255),
    created_at datetime not null,
    primary key (username)
);

create table user_post(
	id bigint primary key auto_increment,
	username varchar(255),
    content varchar(255) not null,
    created datetime not null,
    foreign key (username) references user(username)
);

create table user_follower(
	id bigint primary key auto_increment,
    username varchar(255),
    follower varchar(255),
    foreign key (username) references user(username) on delete cascade,
    foreign key (follower) references user(username) on delete cascade,
    unique (username,follower)
);


insert into user(username, created_at) values ('Alice', '2018-02-01 09:00:00.000');
insert into user(username, created_at) values ('Bob', '2018-02-01 09:00:00.000');
insert into user(username, created_at) values ('Charlie', '2018-02-01 09:00:00.000');
insert into user(username, created_at) values ('Samuel', '2018-02-03 09:00:00.000');


insert into user_post (username, content, created) values ('Alice', 'I feel good', '2018-02-01 09:10:00.000');
insert into user_post (username, content, created) values ('Bob', 'I feel sad', '2018-02-01 10:10:00.000');
insert into user_post (username, content, created) values ('Bob', 'Thank God is Monday. What!?!', '2018-02-02 10:10:00.000');
insert into user_post (username, content, created) values ('Charlie', 'Thank God is Monday.', '2018-02-02 10:08:00.000');

insert into user_follower (username,follower)values ('Alice','Bob');
insert into user_follower (username, follower) values('Charlie','Bob');
insert into user_follower (username, follower) values('Bob','Charlie');