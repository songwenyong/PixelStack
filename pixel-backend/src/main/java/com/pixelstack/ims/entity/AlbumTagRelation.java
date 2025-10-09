package com.pixelstack.ims.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_album_tag_relation")
public class AlbumTagRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long albumId;

    private Long tagId;

    private Long creator;

    private Long updater;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}