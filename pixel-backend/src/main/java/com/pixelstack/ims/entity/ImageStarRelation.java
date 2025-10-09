package com.pixelstack.ims.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_image_star_relation")
public class ImageStarRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long imageId;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}