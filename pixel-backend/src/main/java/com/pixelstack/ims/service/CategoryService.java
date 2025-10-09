package com.pixelstack.ims.service;

import com.pixelstack.ims.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getCategoryTree();

    CategoryDTO createCategory(String categoryName, Long parentId, Long userId);

    void deleteCategory(Long categoryId, Long userId);
}