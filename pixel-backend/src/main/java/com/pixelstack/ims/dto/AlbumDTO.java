package com.pixelstack.ims.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AlbumDTO {
    private Long id;
    private String albumName;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long coverImageId;
    private String coverImageUrl;
    private Long creator;
    private String creatorName;
    private LocalDateTime createdAt;
    private Integer imageCount;
    private Boolean isStared;
    private Integer starCount;
    private List<String> tagNames;
    private List<ImageInfoDTO> images;
}