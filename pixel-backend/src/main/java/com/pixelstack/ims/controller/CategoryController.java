package com.pixelstack.ims.controller;

import com.pixelstack.ims.dto.CategoryDTO;
import com.pixelstack.ims.service.CategoryService;
import com.pixelstack.ims.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/tree")
    public Result<List<CategoryDTO>> getCategoryTree() {
        List<CategoryDTO> categories = categoryService.getCategoryTree();
        return Result.success(categories);
    }

    @PostMapping
    public Result<CategoryDTO> createCategory(@RequestParam String categoryName,
                                             @RequestParam(required = false) Long parentId,
                                             HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        CategoryDTO category = categoryService.createCategory(categoryName, parentId, userId);
        return Result.success(category);
    }

    @DeleteMapping("/{categoryId}")
    public Result<Void> deleteCategory(@PathVariable Long categoryId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        categoryService.deleteCategory(categoryId, userId);
        return Result.success();
    }
}