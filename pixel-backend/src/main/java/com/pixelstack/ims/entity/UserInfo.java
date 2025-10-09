package com.pixelstack.ims.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user_info")
public class UserInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private Integer roleType;

    private String email;

    private String phone;

    @TableLogic
    private String isDelete;

    private String introduction;

    private Long creator;

    private Long updater;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}