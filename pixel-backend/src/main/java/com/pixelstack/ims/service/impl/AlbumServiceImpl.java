package com.pixelstack.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pixelstack.ims.dto.AlbumDTO;
import com.pixelstack.ims.dto.CreateAlbumRequest;
import com.pixelstack.ims.dto.ImageInfoDTO;
import com.pixelstack.ims.dto.PageResult;
import com.pixelstack.ims.dto.UpdateAlbumRequest;
import com.pixelstack.ims.entity.*;
import com.pixelstack.ims.exception.BusinessException;
import com.pixelstack.ims.mapper.*;
import com.pixelstack.ims.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumMapper albumMapper;
    private final AlbumImageRelationMapper albumImageRelationMapper;
    private final AlbumTagRelationMapper albumTagRelationMapper;
    private final AlbumStarRelationMapper albumStarRelationMapper;
    private final TagMapper tagMapper;
    private final com.pixelstack.ims.service.CategoryService categoryService;

    @Override
    @Transactional
    public AlbumDTO createAlbum(CreateAlbumRequest request, Long userId) {
        Album album = new Album();
        album.setAlbumName(request.getAlbumName());
        album.setDescription(request.getDescription());
        album.setCategoryId(request.getCategoryId());
        album.setCoverImageId(request.getCoverImageId());
        album.setCreator(userId);

        albumMapper.insert(album);

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            for (String tagName : request.getTagNames()) {
                LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Tag::getTagName, tagName);
                Tag tag = tagMapper.selectOne(wrapper);

                if (tag == null) {
                    tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setCreator(userId);
                    tagMapper.insert(tag);
                }

                AlbumTagRelation relation = new AlbumTagRelation();
                relation.setAlbumId(album.getId());
                relation.setTagId(tag.getId());
                relation.setCreator(userId);
                albumTagRelationMapper.insert(relation);
            }
        }

        return getAlbumDetail(album.getId(), userId);
    }

    @Override
    public PageResult<AlbumDTO> getAlbumPage(Integer current, Integer size, Long categoryId, String keyword, Long userId) {
        Page<AlbumDTO> page = new Page<>(current, size);

        // Get all descendant category IDs if categoryId is provided
        List<Long> categoryIds = null;
        if (categoryId != null) {
            categoryIds = categoryService.getAllDescendantCategoryIds(categoryId);
        }

        IPage<AlbumDTO> result = albumMapper.selectAlbumPage(page, userId, categoryIds, keyword);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    public PageResult<AlbumDTO> getStaredAlbumPage(Integer current, Integer size, Long userId) {
        Page<AlbumDTO> page = new Page<>(current, size);
        IPage<AlbumDTO> result = albumMapper.selectStaredAlbumPage(page, userId);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    public AlbumDTO getAlbumDetail(Long albumId, Long userId) {
        return albumMapper.selectAlbumDetail(albumId, userId);
    }

    @Override
    public PageResult<ImageInfoDTO> getAlbumImages(Long albumId, Integer current, Integer size, Long userId) {
        Page<ImageInfoDTO> page = new Page<>(current, size);
        IPage<ImageInfoDTO> result = albumMapper.selectAlbumImages(page, albumId, userId);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    @Transactional
    public void addImageToAlbum(Long albumId, List<Long> imageIds, Long userId) {
        Album album = albumMapper.selectById(albumId);
        if (album == null) {
            throw new BusinessException("相册不存在");
        }

        if (!album.getCreator().equals(userId)) {
            throw new BusinessException("无权限操作该相册");
        }

        for (Long imageId : imageIds) {
            LambdaQueryWrapper<AlbumImageRelation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AlbumImageRelation::getAlbumId, albumId)
                   .eq(AlbumImageRelation::getImageId, imageId);

            if (albumImageRelationMapper.selectOne(wrapper) == null) {
                AlbumImageRelation relation = new AlbumImageRelation();
                relation.setAlbumId(albumId);
                relation.setImageId(imageId);
                relation.setCreator(userId);
                albumImageRelationMapper.insert(relation);
            }
        }
    }

    @Override
    public void removeImageFromAlbum(Long albumId, Long imageId, Long userId) {
        Album album = albumMapper.selectById(albumId);
        if (album == null) {
            throw new BusinessException("相册不存在");
        }

        if (!album.getCreator().equals(userId)) {
            throw new BusinessException("无权限操作该相册");
        }

        LambdaQueryWrapper<AlbumImageRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AlbumImageRelation::getAlbumId, albumId)
               .eq(AlbumImageRelation::getImageId, imageId);
        albumImageRelationMapper.delete(wrapper);
    }

    @Override
    public void starAlbum(Long albumId, Long userId) {
        LambdaQueryWrapper<AlbumStarRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AlbumStarRelation::getAlbumId, albumId)
               .eq(AlbumStarRelation::getUserId, userId);

        if (albumStarRelationMapper.selectOne(wrapper) != null) {
            throw new BusinessException("已收藏该相册");
        }

        AlbumStarRelation relation = new AlbumStarRelation();
        relation.setAlbumId(albumId);
        relation.setUserId(userId);
        albumStarRelationMapper.insert(relation);
    }

    @Override
    public void unstarAlbum(Long albumId, Long userId) {
        LambdaQueryWrapper<AlbumStarRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AlbumStarRelation::getAlbumId, albumId)
               .eq(AlbumStarRelation::getUserId, userId);
        albumStarRelationMapper.delete(wrapper);
    }

    @Override
    @Transactional
    public AlbumDTO updateAlbum(UpdateAlbumRequest request, Long userId) {
        // Validate album exists
        Album album = albumMapper.selectById(request.getId());
        if (album == null) {
            throw new BusinessException("相册不存在");
        }

        // Validate user ownership
        if (!album.getCreator().equals(userId)) {
            throw new BusinessException("无权限操作该相册");
        }

        // Update album basic info
        album.setAlbumName(request.getAlbumName());
        album.setDescription(request.getDescription());
        album.setCategoryId(request.getCategoryId());
        album.setCoverImageId(request.getCoverImageId());
        albumMapper.updateById(album);

        // Update tags: delete old ones and add new ones
        LambdaQueryWrapper<AlbumTagRelation> tagWrapper = new LambdaQueryWrapper<>();
        tagWrapper.eq(AlbumTagRelation::getAlbumId, album.getId());
        albumTagRelationMapper.delete(tagWrapper);

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            for (String tagName : request.getTagNames()) {
                LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Tag::getTagName, tagName);
                Tag tag = tagMapper.selectOne(wrapper);

                if (tag == null) {
                    tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setCreator(userId);
                    tagMapper.insert(tag);
                }

                AlbumTagRelation relation = new AlbumTagRelation();
                relation.setAlbumId(album.getId());
                relation.setTagId(tag.getId());
                relation.setCreator(userId);
                albumTagRelationMapper.insert(relation);
            }
        }

        // Update album images if imageIds list is provided
        if (request.getImageIds() != null) {
            // Delete all existing album-image relations
            LambdaQueryWrapper<AlbumImageRelation> imageWrapper = new LambdaQueryWrapper<>();
            imageWrapper.eq(AlbumImageRelation::getAlbumId, album.getId());
            albumImageRelationMapper.delete(imageWrapper);

            // Add new album-image relations
            for (Long imageId : request.getImageIds()) {
                AlbumImageRelation relation = new AlbumImageRelation();
                relation.setAlbumId(album.getId());
                relation.setImageId(imageId);
                relation.setCreator(userId);
                albumImageRelationMapper.insert(relation);
            }
        }

        return getAlbumDetail(album.getId(), userId);
    }

    @Override
    @Transactional
    public void deleteAlbum(Long albumId, Long userId) {
        Album album = albumMapper.selectById(albumId);
        if (album == null) {
            throw new BusinessException("相册不存在");
        }

        if (!album.getCreator().equals(userId)) {
            throw new BusinessException("无权限删除该相册");
        }

        LambdaQueryWrapper<AlbumImageRelation> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.eq(AlbumImageRelation::getAlbumId, albumId);
        albumImageRelationMapper.delete(imageWrapper);

        LambdaQueryWrapper<AlbumTagRelation> tagWrapper = new LambdaQueryWrapper<>();
        tagWrapper.eq(AlbumTagRelation::getAlbumId, albumId);
        albumTagRelationMapper.delete(tagWrapper);

        LambdaQueryWrapper<AlbumStarRelation> starWrapper = new LambdaQueryWrapper<>();
        starWrapper.eq(AlbumStarRelation::getAlbumId, albumId);
        albumStarRelationMapper.delete(starWrapper);

        albumMapper.deleteById(albumId);
    }
}