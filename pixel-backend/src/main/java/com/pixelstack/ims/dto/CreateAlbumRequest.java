package com.pixelstack.ims.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateAlbumRequest {

    @NotBlank(message = "相册名称不能为空")
    private String albumName;

    private Long categoryId;

    @NotNull(message = "封面图片不能为空")
    private Long coverImageId;

    private List<String> tagNames;
}