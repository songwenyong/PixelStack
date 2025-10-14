package com.pixelstack.ims.service;

import com.pixelstack.ims.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getCategoryTree();

    CategoryDTO createCategory(String categoryName, Long parentId, Long userId);

    CategoryDTO updateCategory(Long categoryId, String categoryName, Long parentId, Long userId);

    void deleteCategory(Long categoryId, Long userId);

    /**
     * Get all descendant category IDs (including the category itself)
     * @param categoryId The category ID
     * @return List of all descendant category IDs including the category itself
     */
    List<Long> getAllDescendantCategoryIds(Long categoryId);
}