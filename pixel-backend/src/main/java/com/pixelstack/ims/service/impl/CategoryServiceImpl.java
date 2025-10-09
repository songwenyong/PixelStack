package com.pixelstack.ims.service.impl;

import com.pixelstack.ims.dto.CategoryDTO;
import com.pixelstack.ims.entity.Category;
import com.pixelstack.ims.exception.BusinessException;
import com.pixelstack.ims.mapper.CategoryMapper;
import com.pixelstack.ims.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getCategoryTree() {
        return categoryMapper.selectCategoryTree();
    }

    @Override
    public CategoryDTO createCategory(String categoryName, Long parentId, Long userId) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setParentId(parentId);
        category.setCreator(userId);

        categoryMapper.insert(category);

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        dto.setParentId(category.getParentId());
        dto.setCreatedAt(category.getCreatedAt());

        return dto;
    }

    @Override
    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            throw new BusinessException("类目不存在");
        }

        if (!category.getCreator().equals(userId)) {
            throw new BusinessException("无权限删除该类目");
        }

        categoryMapper.deleteById(categoryId);
    }
}