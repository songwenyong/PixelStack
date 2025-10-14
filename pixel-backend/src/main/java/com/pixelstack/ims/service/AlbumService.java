package com.pixelstack.ims.service;

import com.pixelstack.ims.dto.AlbumDTO;
import com.pixelstack.ims.dto.CreateAlbumRequest;
import com.pixelstack.ims.dto.ImageInfoDTO;
import com.pixelstack.ims.dto.PageResult;
import com.pixelstack.ims.dto.UpdateAlbumRequest;

import java.util.List;

public interface AlbumService {

    AlbumDTO createAlbum(CreateAlbumRequest request, Long userId);

    AlbumDTO updateAlbum(UpdateAlbumRequest request, Long userId);

    PageResult<AlbumDTO> getAlbumPage(Integer current, Integer size, Long categoryId, String keyword, Long userId);

    PageResult<AlbumDTO> getStaredAlbumPage(Integer current, Integer size, Long userId);

    AlbumDTO getAlbumDetail(Long albumId, Long userId);

    PageResult<ImageInfoDTO> getAlbumImages(Long albumId, Integer current, Integer size, Long userId);

    void addImageToAlbum(Long albumId, List<Long> imageIds, Long userId);

    void removeImageFromAlbum(Long albumId, Long imageId, Long userId);

    void starAlbum(Long albumId, Long userId);

    void unstarAlbum(Long albumId, Long userId);

    void deleteAlbum(Long albumId, Long userId);
}