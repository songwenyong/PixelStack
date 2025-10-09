package com.pixelstack.ims.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_tag")
public class Tag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String tagName;

    private Long creator;

    private Long updater;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}