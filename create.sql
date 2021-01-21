create sequence hibernate_sequence start with 1 increment by 1
create table comments (id bigint not null, body varchar(255), post_id bigint not null, user_id bigint, primary key (id))
create table post_authors (post_id bigint not null, user_id bigint not null)
create table post_tags (post_id bigint not null, tag_id bigint not null)
create table posts (id bigint not null, isprivate boolean not null, post_content varchar(9999), title varchar(255), primary key (id))
create table roles (id bigint not null, role varchar(255), primary key (id))
create table tags (id bigint not null, name varchar(255), primary key (id))
create table user_roles (user_id bigint not null, role_id bigint not null)
create table users (id bigint not null, active boolean not null, email varchar(255), password varchar(255), username varchar(30), primary key (id))
alter table users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email)
alter table comments add constraint FKh4c7lvsc298whoyd4w9ta25cr foreign key (post_id) references posts
alter table comments add constraint FK8omq0tc18jd43bu5tjh6jvraq foreign key (user_id) references users
alter table post_authors add constraint FKk9a90al1x1log5n97br3xnmjm foreign key (user_id) references users
alter table post_authors add constraint FK77ahri1kivqai91wuqgn1e7ca foreign key (post_id) references posts
alter table post_tags add constraint FKm6cfovkyqvu5rlm6ahdx3eavj foreign key (tag_id) references tags
alter table post_tags add constraint FKkifam22p4s1nm3bkmp1igcn5w foreign key (post_id) references posts
alter table user_roles add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles
alter table user_roles add constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users
