package com.pixelstack.ims.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_album")
public class Album {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String albumName;

    private Long categoryId;

    private Long coverImageId;

    private Long creator;

    private Long updater;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}