package com.pixelstack.ims.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageInfoDTO {
    private Long id;
    private String title;
    private String format;
    private String url;
    private String thumbnailUrl;
    private Long creator;
    private String creatorName;
    private LocalDateTime createdAt;
    private Boolean isStared;
}