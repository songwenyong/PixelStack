package com.pixelstack.ims.controller;

import com.pixelstack.ims.dto.ImageInfoDTO;
import com.pixelstack.ims.dto.PageResult;
import com.pixelstack.ims.service.ImageService;
import com.pixelstack.ims.util.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public Result<ImageInfoDTO> uploadImage(@RequestParam("file") MultipartFile file,
                                           @RequestParam(value = "title", required = false) String title,
                                           HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("Image upload attempt: userId={}, filename={}, size={}", userId, file.getOriginalFilename(), file.getSize());
        ImageInfoDTO imageInfo = imageService.uploadImage(file, title, userId);
        log.info("Image upload successful: userId={}, imageId={}", userId, imageInfo.getId());
        return Result.success(imageInfo);
    }

    @GetMapping("/page")
    public Result<PageResult<ImageInfoDTO>> getImagePage(@RequestParam(defaultValue = "1") Integer current,
                                                        @RequestParam(defaultValue = "50") Integer size,
                                                        @RequestParam(required = false) Long categoryId,
                                                        @RequestParam(required = false) String keyword,
                                                        HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<ImageInfoDTO> result = imageService.getImagePage(current, size, categoryId, keyword, userId);
        return Result.success(result);
    }

    @GetMapping("/stared/page")
    public Result<PageResult<ImageInfoDTO>> getStaredImagePage(@RequestParam(defaultValue = "1") Integer current,
                                                              @RequestParam(defaultValue = "50") Integer size,
                                                              HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<ImageInfoDTO> result = imageService.getStaredImagePage(current, size, userId);
        return Result.success(result);
    }

    @PostMapping("/{imageId}/star")
    public Result<Void> starImage(@PathVariable Long imageId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        imageService.starImage(imageId, userId);
        return Result.success();
    }

    @DeleteMapping("/{imageId}/star")
    public Result<Void> unstarImage(@PathVariable Long imageId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        imageService.unstarImage(imageId, userId);
        return Result.success();
    }

    @DeleteMapping("/{imageId}")
    public Result<Void> deleteImage(@PathVariable Long imageId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        imageService.deleteImage(imageId, userId);
        return Result.success();
    }
}