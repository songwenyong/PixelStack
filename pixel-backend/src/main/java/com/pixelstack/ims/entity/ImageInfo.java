package com.pixelstack.ims.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_image_info")
public class ImageInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String format;

    private String md5;

    private String url;

    private String thumbnailUrl;

    @TableLogic
    private Integer isDelete;

    private Long creator;

    private Long updater;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}