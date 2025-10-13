package com.pixelstack.ims.service;

import com.pixelstack.ims.dto.ImageInfoDTO;
import com.pixelstack.ims.dto.PageResult;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ImageInfoDTO uploadImage(MultipartFile file, String title, Long userId);

    PageResult<ImageInfoDTO> getImagePage(Integer current, Integer size, Long categoryId, String keyword, Long userId);

    PageResult<ImageInfoDTO> getStaredImagePage(Integer current, Integer size, Long userId);

    void starImage(Long imageId, Long userId);

    void unstarImage(Long imageId, Long userId);

    void deleteImage(Long imageId, Long userId);
}