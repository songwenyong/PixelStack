create table t_image_info
(
    id            bigint unsigned auto_increment comment '图片id'
        primary key,
    title         varchar(64) null comment '图片标题',
    format        varchar(64) null comment '图片格式',
    md5           varchar(64) null comment '图片MD5',
    url           varchar(256) null comment '图片路径',
    thumbnail_url varchar(256) null comment '缩略图路径',
    is_delete     tinyint(1) default 0                 null comment '是否删除 0 未删除 1已删除',
    creator       bigint unsigned                      not null comment '创建人id',
    updater       bigint unsigned                      null comment '修改人id',
    created_at    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at    datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    unique (md5)
) comment '图片信息表';

create table t_album
(
    id             bigint unsigned auto_increment comment '相册id'
        primary key,
    album_name     varchar(64) null comment '相册名称',
    category_id    bigint                             not null default 0 comment '相册所属类目,t_category表主键',
    cover_image_id bigint                             not null comment '相册封面图片id,t_image_info表主键',
    creator        bigint unsigned                    not null comment '创建人id',
    updater        bigint unsigned                    null comment '修改人id',
    created_at     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at     datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '相册表';

create table t_album_image_relation
(
    id         bigint unsigned auto_increment comment '关系id'
        primary key,
    album_id   bigint                             not null comment '相册id',
    image_id   bigint                             not null comment '图片id',
    creator    bigint unsigned                    not null comment '创建人id',
    updater    bigint unsigned                    null comment '修改人id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    unique (album_id, image_id)
) comment '图片和相册关系表，相册和图片为一对多关系';

create table t_category
(
    id            bigint unsigned auto_increment comment '类目id'
        primary key,
    parent_id     bigint null comment '父级类目id，如果是一级类目则为null',
    category_name varchar(64) null comment '类目名称',
    creator       bigint unsigned                    not null comment '创建人id',
    updater       bigint unsigned                    null comment '修改用户id',
    created_at    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at    datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '类目表';


create table t_tag
(
    id         bigint unsigned auto_increment comment '标签id'
        primary key,
    tag_name   varchar(16) null comment '标签名称',
    creator    bigint unsigned                    not null comment '创建人id',
    updater    bigint unsigned                    null comment '修改人id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '标签表';

create table t_album_tag_relation
(
    id         bigint unsigned auto_increment comment '关系id'
        primary key,
    album_id   bigint                             not null comment '相册id',
    tag_id     bigint                             not null comment '标签id',
    creator    bigint unsigned                    not null comment '创建人id',
    updater    bigint unsigned                    null comment '修改人id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    unique (album_id, tag_id)
) comment '标签关系表';

create table t_user_info
(
    id           bigint unsigned auto_increment comment '用户id'
        primary key,
    username     varchar(16) not null comment '用户名',
    password     varchar(16) not null comment '密码',
    role_type    int         not null default 1 comment '权限角色，0管理员 1普通用户',
    email        varchar(128) not null comment '邮箱地址',
    phone        varchar(16) not null default '' comment '手机号码',
    is_delete    varchar(16) null comment '账户状态 1已删除 0正常',
    introduction varchar(256) null comment '个人简介',
    creator      bigint unsigned not null comment '创建人',
    updater      bigint unsigned null comment '修改人',
    created_at   datetime             default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at   datetime             default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '用户信息表';

create table t_image_star_relation
(
    id         bigint unsigned auto_increment comment '关系id'
        primary key,
    image_id   bigint                             not null comment '图片id',
    user_id    bigint                             not null comment '用户id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '图片收藏关系表';


create table t_album_star_relation
(
    id         bigint unsigned auto_increment comment '关系id'
        primary key,
    album_id   bigint                             not null comment '图片专辑id',
    user_id    bigint                             not null comment '用户id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '修改时间'
) comment '相册收藏关系表';