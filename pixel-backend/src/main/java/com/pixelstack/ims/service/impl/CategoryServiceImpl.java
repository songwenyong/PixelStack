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
        // Get all categories as flat list
        List<CategoryDTO> allCategories = categoryMapper.selectCategoryTree();

        // Build tree structure
        return buildTree(allCategories, null);
    }

    /**
     * Build hierarchical tree from flat list of categories
     * @param allCategories All categories
     * @param parentId Parent ID to filter by (null for root)
     * @return List of categories at this level with their children populated
     */
    private List<CategoryDTO> buildTree(List<CategoryDTO> allCategories, Long parentId) {
        List<CategoryDTO> result = new java.util.ArrayList<>();

        for (CategoryDTO category : allCategories) {
            // Match categories with the specified parent ID
            boolean isMatch = (parentId == null && category.getParentId() == null) ||
                            (parentId != null && parentId.equals(category.getParentId()));

            if (isMatch) {
                // Recursively find and set children
                List<CategoryDTO> children = buildTree(allCategories, category.getId());
                if (!children.isEmpty()) {
                    category.setChildren(children);
                }
                result.add(category);
            }
        }

        return result;
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
    public CategoryDTO updateCategory(Long categoryId, String categoryName, Long parentId, Long userId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            throw new BusinessException("类目不存在");
        }

        if (!category.getCreator().equals(userId)) {
            throw new BusinessException("无权限修改该类目");
        }

        // Prevent setting parent to self
        if (categoryId.equals(parentId)) {
            throw new BusinessException("不能将类目设置为自己的父类目");
        }

        // Prevent circular reference
        if (parentId != null && isCircularReference(categoryId, parentId)) {
            throw new BusinessException("不能将类目设置为其子类目的父类目");
        }

        category.setCategoryName(categoryName);
        category.setParentId(parentId);
        category.setUpdater(userId);

        categoryMapper.updateById(category);

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

    @Override
    public List<Long> getAllDescendantCategoryIds(Long categoryId) {
        List<Long> result = new java.util.ArrayList<>();
        result.add(categoryId); // Include the category itself
        collectDescendantIds(categoryId, result);
        return result;
    }

    /**
     * Recursively collect all descendant category IDs
     */
    private void collectDescendantIds(Long parentId, List<Long> result) {
        List<Category> children = categoryMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Category>()
                .eq("parent_id", parentId)
        );

        for (Category child : children) {
            result.add(child.getId());
            collectDescendantIds(child.getId(), result); // Recursively collect children
        }
    }

    /**
     * Check if setting parentId would create a circular reference
     */
    private boolean isCircularReference(Long categoryId, Long parentId) {
        Long currentParentId = parentId;
        while (currentParentId != null) {
            if (currentParentId.equals(categoryId)) {
                return true;
            }
            Category parent = categoryMapper.selectById(currentParentId);
            if (parent == null) {
                break;
            }
            currentParentId = parent.getParentId();
        }
        return false;
    }
}