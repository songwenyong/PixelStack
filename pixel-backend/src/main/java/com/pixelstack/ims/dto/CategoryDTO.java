package com.pixelstack.ims.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private Long parentId;
    private String categoryName;
    private LocalDateTime createdAt;
    private List<CategoryDTO> children;
    private Integer albumCount;
}