package com.pixelstack.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pixelstack.ims.dto.CategoryDTO;
import com.pixelstack.ims.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryDTO> selectCategoryTree();
}