package com.pixelstack.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pixelstack.ims.dto.ImageInfoDTO;
import com.pixelstack.ims.entity.ImageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImageInfoMapper extends BaseMapper<ImageInfo> {

    IPage<ImageInfoDTO> selectImagePage(Page<ImageInfoDTO> page, @Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("keyword") String keyword);

    IPage<ImageInfoDTO> selectStaredImagePage(Page<ImageInfoDTO> page, @Param("userId") Long userId);
}