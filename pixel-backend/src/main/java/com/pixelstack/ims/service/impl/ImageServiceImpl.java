package com.pixelstack.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pixelstack.ims.dto.ImageInfoDTO;
import com.pixelstack.ims.dto.PageResult;
import com.pixelstack.ims.entity.ImageInfo;
import com.pixelstack.ims.entity.ImageStarRelation;
import com.pixelstack.ims.exception.BusinessException;
import com.pixelstack.ims.mapper.ImageInfoMapper;
import com.pixelstack.ims.mapper.ImageStarRelationMapper;
import com.pixelstack.ims.service.ImageService;
import com.pixelstack.ims.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageInfoMapper imageInfoMapper;
    private final ImageStarRelationMapper imageStarRelationMapper;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    @Override
    public ImageInfoDTO uploadImage(MultipartFile file, String title, Long userId) {
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        try {
            String md5 = calculateMD5(file.getBytes());

            LambdaQueryWrapper<ImageInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ImageInfo::getMd5, md5);
            ImageInfo existingImage = imageInfoMapper.selectOne(wrapper);

            if (existingImage != null) {
                throw new BusinessException("图片已存在");
            }

            String fileName = FileUtil.saveFile(file, uploadPath);
            String format = FileUtil.getFileExtension(file.getOriginalFilename());

            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setTitle(StringUtils.isBlank(title) ? file.getOriginalFilename() : title);
            imageInfo.setFormat(format);
            imageInfo.setMd5(md5);
            imageInfo.setUrl(urlPrefix + fileName);
            imageInfo.setThumbnailUrl(urlPrefix + fileName);
            imageInfo.setCreator(userId);

            imageInfoMapper.insert(imageInfo);

            ImageInfoDTO dto = new ImageInfoDTO();
            dto.setId(imageInfo.getId());
            dto.setTitle(imageInfo.getTitle());
            dto.setFormat(imageInfo.getFormat());
            dto.setUrl(imageInfo.getUrl());
            dto.setThumbnailUrl(imageInfo.getThumbnailUrl());
            dto.setCreator(imageInfo.getCreator());
            dto.setCreatedAt(imageInfo.getCreatedAt());
            dto.setIsStared(false);

            return dto;
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new BusinessException("文件上传失败");
        }
    }

    @Override
    public PageResult<ImageInfoDTO> getImagePage(Integer current, Integer size, String keyword, Long userId) {
        Page<ImageInfoDTO> page = new Page<>(current, size);
        IPage<ImageInfoDTO> result = imageInfoMapper.selectImagePage(page, userId, keyword);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    public PageResult<ImageInfoDTO> getStaredImagePage(Integer current, Integer size, Long userId) {
        Page<ImageInfoDTO> page = new Page<>(current, size);
        IPage<ImageInfoDTO> result = imageInfoMapper.selectStaredImagePage(page, userId);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    public void starImage(Long imageId, Long userId) {
        LambdaQueryWrapper<ImageStarRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImageStarRelation::getImageId, imageId)
               .eq(ImageStarRelation::getUserId, userId);

        if (imageStarRelationMapper.selectOne(wrapper) != null) {
            throw new BusinessException("已收藏该图片");
        }

        ImageStarRelation relation = new ImageStarRelation();
        relation.setImageId(imageId);
        relation.setUserId(userId);
        imageStarRelationMapper.insert(relation);
    }

    @Override
    public void unstarImage(Long imageId, Long userId) {
        LambdaQueryWrapper<ImageStarRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImageStarRelation::getImageId, imageId)
               .eq(ImageStarRelation::getUserId, userId);
        imageStarRelationMapper.delete(wrapper);
    }

    @Override
    public void deleteImage(Long imageId, Long userId) {
        ImageInfo imageInfo = imageInfoMapper.selectById(imageId);
        if (imageInfo == null) {
            throw new BusinessException("图片不存在");
        }

        if (!imageInfo.getCreator().equals(userId)) {
            throw new BusinessException("无权限删除该图片");
        }

        imageInfoMapper.deleteById(imageId);
    }

    private String calculateMD5(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}