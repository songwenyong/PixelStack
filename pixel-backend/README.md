# IMS Backend - Image Management System

## 项目简介

基于SpringBoot 2.7.18构建的图片管理系统后端，提供完整的图片管理、相册管理、分类管理、标签管理和用户管理功能。

## 技术栈

- **Java 1.8**
- **SpringBoot 2.7.18**
- **MySQL 8.0.13**
- **Redis** - 缓存和会话管理
- **MyBatis Plus** - ORM框架
- **JWT** - 用户认证
- **FastJSON** - JSON序列化
- **Lombok** - 简化Java代码

## 功能模块

### 1. 用户管理模块
- 用户注册/登录
- JWT身份认证
- 用户信息管理
- 权限控制（管理员/普通用户）

### 2. 图片管理模块
- 图片上传（支持MD5去重）
- 图片列表查询（支持关键词搜索）
- 图片收藏/取消收藏
- 图片删除

### 3. 相册管理模块
- 相册创建/删除
- 相册图片管理
- 相册分页查询
- 相册收藏功能
- 相册标签关联

### 4. 分类管理模块
- 层级分类结构
- 分类树查询
- 分类创建/删除

### 5. 标签管理模块
- 标签创建
- 相册标签关联

### 6. 社交互动模块
- 图片收藏
- 相册收藏

## API接口

### 用户相关
- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 用户注册
- `GET /api/user/current` - 获取当前用户信息
- `PUT /api/user/update` - 更新用户信息

### 图片相关
- `POST /api/image/upload` - 上传图片
- `GET /api/image/page` - 分页查询图片
- `GET /api/image/stared/page` - 查询收藏的图片
- `POST /api/image/{imageId}/star` - 收藏图片
- `DELETE /api/image/{imageId}/star` - 取消收藏图片
- `DELETE /api/image/{imageId}` - 删除图片

### 相册相关
- `POST /api/album` - 创建相册
- `GET /api/album/page` - 分页查询相册
- `GET /api/album/stared/page` - 查询收藏的相册
- `GET /api/album/{albumId}` - 获取相册详情
- `POST /api/album/{albumId}/images` - 添加图片到相册
- `DELETE /api/album/{albumId}/images/{imageId}` - 从相册移除图片
- `POST /api/album/{albumId}/star` - 收藏相册
- `DELETE /api/album/{albumId}/star` - 取消收藏相册
- `DELETE /api/album/{albumId}` - 删除相册

### 分类相关
- `GET /api/category/tree` - 获取分类树
- `POST /api/category` - 创建分类
- `DELETE /api/category/{categoryId}` - 删除分类

### 文件相关
- `POST /api/files/upload` - 文件上传
- `GET /api/files/**` - 文件访问

## 数据库设计

### 核心表结构
- `t_user_info` - 用户信息表
- `t_image_info` - 图片信息表
- `t_album` - 相册表
- `t_category` - 分类表
- `t_tag` - 标签表
- `t_album_image_relation` - 相册图片关系表
- `t_album_tag_relation` - 相册标签关系表
- `t_image_star_relation` - 图片收藏关系表
- `t_album_star_relation` - 相册收藏关系表

## 配置说明

### 应用配置
- 服务端口：8080
- 上下文路径：/api
- 文件上传大小限制：10MB

### 数据库配置
- 默认数据库：ims
- 连接池：HikariCP
- 最大连接数：20

### Redis配置
- 默认端口：6379
- 数据库：0

### JWT配置
- 密钥：配置在application.yml中
- 过期时间：7天

## 启动方式

1. 确保MySQL和Redis服务已启动
2. 创建数据库并执行sql/ims.sql建表脚本
3. 修改application.yml中的数据库连接配置
4. 运行主类：`ImsBackendApplication`

## 开发环境

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

## 注意事项

1. 所有需要认证的接口都需要在请求头中携带`Authorization: Bearer {token}`
2. 文件上传目录需要确保应用有读写权限
3. 图片上传支持MD5去重，相同文件不会重复存储
4. 所有时间字段使用LocalDateTime，自动填充创建时间和更新时间