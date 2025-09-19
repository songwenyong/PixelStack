create table tb_comment
(
    cid     int auto_increment
        primary key,
    uid     int         null,
    content varchar(64) not null,
    cdate   date        null
);

create index uid
    on tb_comment (uid);

create table tb_comment_relate
(
    id  int auto_increment
        primary key,
    iid int(10) not null,
    cid int(10) not null
);

create index cid
    on tb_comment_relate (cid);

create index iid
    on tb_comment_relate (iid);

create table tb_follow_relate
(
    id  int auto_increment
        primary key,
    uid int(10) not null,
    fid int(10) not null
);

create index fid
    on tb_follow_relate (fid);

create index uid
    on tb_follow_relate (uid);

create table tb_image_info
(
    iid    int auto_increment
        primary key,
    title  varchar(64)   null,
    author varchar(16)   not null,
    upload date          null,
    url    varchar(256)  null,
    count  int default 0 null
);

create table tb_star_relate
(
    id  int auto_increment
        primary key,
    uid int(10) not null,
    iid int(10) not null
);

create index iid
    on tb_star_relate (iid);

create index uid
    on tb_star_relate (uid);

create table tb_tag
(
    tid     int auto_increment
        primary key,
    tagname varchar(16) null
);

create table tb_tag_relate
(
    id  int auto_increment
        primary key,
    iid int not null,
    tid int not null
);

create index iid
    on tb_tag_relate (iid);

create index tid
    on tb_tag_relate (tid);

create table tb_thumb_relate
(
    id  int auto_increment
        primary key,
    uid int(10) not null,
    iid int(10) not null
);

create index iid
    on tb_thumb_relate (iid);

create index uid
    on tb_thumb_relate (uid);

create table tb_user_info
(
    uid          int auto_increment
        primary key,
    username     varchar(16)                        not null,
    password     varchar(16)                        not null,
    authority    varchar(16)                        null,
    email        varchar(16)                        null,
    status       varchar(16)                        null,
    introduction varchar(256)                       null,
    admindate    datetime default CURRENT_TIMESTAMP not null,
    constraint username
        unique (username)
);

INSERT INTO `tb_user_info`(uid
                          , username
                          , password
                          , authority
                          , email
                          , status
                          , introduction) VALUES (100,'root','root','root','lcan@gmail.com','normal',NULL),(101,'Lcanboom','123456','user','lcan@gmail.com','frozen',NULL),(102,'Yangmi','666','user','YangMi@gmail.com','frozen','fuckkkkk'),(103,'Harden','123456','user','lcan@gmail.com','frozen',NULL),(104,'kobe','123456','user','kobe@gmail.com','frozen','fuckkkkk'),(105,'Har','123456','user','lcan@gmail.com','frozen',NULL),(106,'XXXXG','666','user','XXXG@gmail.com','normal',NULL),(107,'XXXA','521','user','lcan@gmail.com','normal',NULL),(108,'VAVA','888','user','lcan@gmail.com','normal',NULL),(109,'Paul','888','user','lcan@gmail.com','normal',NULL),(110,'Dawe','888','user','lcan@gmail.com','normal',NULL),(111,'Curry','888','admin','lcan@gmail.com','normal',NULL);

INSERT INTO `tb_tag` VALUES (101,'风景'),(102,'人物'),(103,'人物');
