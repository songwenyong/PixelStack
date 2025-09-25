create table t_image_info
(
    id            bigint auto_increment unsigned comment '图片ID'
        primary key,
    title         varchar(64) null comment '图片标题',
    format        varchar(64) null comment '图片格式',
    md5           varchar(64) null comment '图片MD5',
    url           varchar(256) null comment '图片路径',
    thumbnail_url varchar(256) null comment '缩略图路径',
    is_delete     tinyint(1) default 0 null comment '是否删除 0 未删除 1已删除',
    creator       bigint unsigned not null comment '上传用户id',
    updater       bigint unsigned null comment '修改这用户id',
    created_at    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at    datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '图片信息表';


create table t_image_info_log
(
    id         bigint auto_increment unsigned comment '图片最新版本ID'
        primary key,
    image_id   bigint unsigned not null comment '图片ID,t_image_info表主键',
    title      varchar(64) null comment '图片标题',
    format     varchar(64) null comment '图片格式',
    md5        varchar(64) null comment '图片MD5',
    url        varchar(256) null comment '图片路径',
    creator    bigint unsigned not null comment '上传用户id',
    updater    bigint unsigned null comment '修改这用户id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '图片信息版本表，每次上传或者编辑图片都会生成一个版本';


create table t_tag
(
    id         bigint auto_increment unsigned comment  '标签ID'
        primary key,
    tag_name   varchar(16) null comment '标签名称',
    creator    bigint unsigned not null comment '上传用户id',
    updater    bigint unsigned null comment '修改这用户id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '标签表';

create table t_tag_relation
(
    id         bigint auto_increment unsigned comment  '关系ID'
        primary key,
    image_id   bigint                             not null comment '图片ID',
    tag_id     bigint                             not null comment '标签ID',
    creator    bigint unsigned not null comment '上传用户id',
    updater    bigint unsigned null comment '修改这用户id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '图片标签关系表';

create table t_album
(
    id         bigint auto_increment unsigned comment  '相册ID'
        primary key,
    album_name varchar(16) null comment '相册名称',
    album_type int                                not null default 0 comment '相册类型：1设计师相册 2产品相册 3其他相册',
    image_id   bigint                             not null comment '相册封面图片ID,t_image_info表主键',
    creator    bigint unsigned not null comment '上传用户id',
    updater    bigint unsigned null comment '修改这用户id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '相册表';


create table t_album_relation
(
    id         bigint auto_increment unsigned comment  '关系ID'
        primary key,
    image_id   bigint                             not null comment '图片ID',
    album_id   bigint                             not null comment '相册ID',
    creator    bigint unsigned not null comment '上传用户id',
    updater    bigint unsigned null comment '修改这用户id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '图片相册关系表';


create table t_user_info
(
    id           bigint auto_increment unsigned comment '用户ID'
        primary key,
    username     varchar(16) not null comment '用户名',
    password     varchar(16) not null comment '密码',
    role_type    int         not null default 1 comment '权限角色，0管理员 1普通用户',
    email        varchar(16) not null comment '邮箱地址',
    phone        varchar(16) not null default '' comment '手机号码',
    is_delete    varchar(16) null comment '账户状态 1已删除 0正常',
    introduction varchar(256) null comment '个人简介',
    creator      bigint unsigned not null comment '创建人',
    updater      bigint unsigned null comment '修改人',
    created_at   datetime             default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at   datetime             default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '用户信息表';

create table t_star_relation
(
    id         bigint auto_increment unsigned comment  '关系ID'
        primary key,
    image_id   bigint                             not null comment '图片ID',
    user_id    bigint                             not null comment '用户ID',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '图片收藏关系表';