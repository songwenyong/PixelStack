package com.pixelstack.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pixelstack.ims.dto.AlbumDTO;
import com.pixelstack.ims.entity.Album;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlbumMapper extends BaseMapper<Album> {

    IPage<AlbumDTO> selectAlbumPage(Page<AlbumDTO> page, @Param("userId") Long userId,
                                   @Param("categoryId") Long categoryId, @Param("keyword") String keyword);

    IPage<AlbumDTO> selectStaredAlbumPage(Page<AlbumDTO> page, @Param("userId") Long userId);

    AlbumDTO selectAlbumDetail(@Param("albumId") Long albumId, @Param("userId") Long userId);
}