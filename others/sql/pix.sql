create table tb_comment
(
    cid     int auto_increment comment '评论ID'
        primary key,
    uid     int null comment '用户ID',
    content varchar(64) not null comment '评论内容',
    cdate   date null comment '评论日期'
) comment '评论表';

create index uid
    on tb_comment (uid);

create table tb_comment_relate
(
    id  int auto_increment comment '关系ID'
        primary key,
    iid int(10) not null comment '图片ID',
    cid int(10) not null comment '评论ID'
) comment '图片评论关系表';

create index cid
    on tb_comment_relate (cid);

create index iid
    on tb_comment_relate (iid);

create table tb_follow_relate
(
    id  int auto_increment comment '关系ID'
        primary key,
    uid int(10) not null comment '用户ID',
    fid int(10) not null comment '被关注用户ID'
) comment '用户关注关系表';

create index fid
    on tb_follow_relate (fid);

create index uid
    on tb_follow_relate (uid);

create table tb_image_info
(
    iid    int auto_increment comment '图片ID'
        primary key,
    title  varchar(64) null comment '图片标题',
    author varchar(16) not null comment '上传者用户名',
    upload date null comment '上传日期',
    url    varchar(256) null comment '图片路径',
    count  int default 0 null comment '浏览次数'
) comment '图片信息表';

create table tb_star_relate
(
    id  int auto_increment comment '关系ID'
        primary key,
    uid int(10) not null comment '用户ID',
    iid int(10) not null comment '图片ID'
) comment '用户收藏图片关系表';

create index iid
    on tb_star_relate (iid);

create index uid
    on tb_star_relate (uid);

create table tb_tag
(
    tid     int auto_increment comment '标签ID'
        primary key,
    tagname varchar(16) null comment '标签名称'
) comment '标签表';

create table tb_tag_relate
(
    id  int auto_increment comment '关系ID'
        primary key,
    iid int not null comment '图片ID',
    tid int not null comment '标签ID'
) comment '图片标签关系表';

create index iid
    on tb_tag_relate (iid);

create index tid
    on tb_tag_relate (tid);

create table tb_thumb_relate
(
    id  int auto_increment comment '关系ID'
        primary key,
    uid int(10) not null comment '用户ID',
    iid int(10) not null comment '图片ID'
) comment '用户点赞图片关系表';

create index iid
    on tb_thumb_relate (iid);

create index uid
    on tb_thumb_relate (uid);

create table tb_user_info
(
    uid          int auto_increment comment '用户ID'
        primary key,
    username     varchar(16)                        not null comment '用户名',
    password     varchar(16)                        not null comment '密码',
    authority    varchar(16) null comment '权限角色',
    email        varchar(16) null comment '邮箱地址',
    status       varchar(16) null comment '账户状态',
    introduction varchar(256) null comment '个人简介',
    admindate    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint username
        unique (username)
) comment '用户信息表';

INSERT INTO `tb_user_info`( uid
                          , username
                          , password
                          , authority
                          , email
                          , status
                          , introduction)
VALUES (100, 'root', 'root', 'root', 'lcan@gmail.com', 'normal', NULL),
       (101, 'Lcanboom', '123456', 'user', 'lcan@gmail.com', 'frozen', NULL),
       (102, 'Yangmi', '666', 'user', 'YangMi@gmail.com', 'frozen', 'fuckkkkk'),
       (103, 'Harden', '123456', 'user', 'lcan@gmail.com', 'frozen', NULL),
       (104, 'kobe', '123456', 'user', 'kobe@gmail.com', 'frozen', 'fuckkkkk'),
       (105, 'Har', '123456', 'user', 'lcan@gmail.com', 'frozen', NULL),
       (106, 'XXXXG', '666', 'user', 'XXXG@gmail.com', 'normal', NULL),
       (107, 'XXXA', '521', 'user', 'lcan@gmail.com', 'normal', NULL),
       (108, 'VAVA', '888', 'user', 'lcan@gmail.com', 'normal', NULL),
       (109, 'Paul', '888', 'user', 'lcan@gmail.com', 'normal', NULL),
       (110, 'Dawe', '888', 'user', 'lcan@gmail.com', 'normal', NULL),
       (111, 'Curry', '888', 'admin', 'lcan@gmail.com', 'normal', NULL);

INSERT INTO `tb_tag`
VALUES (101, '风景'),
       (102, '人物'),
       (103, '人物');
