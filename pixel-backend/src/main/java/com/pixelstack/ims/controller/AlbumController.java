package com.pixelstack.ims.controller;

import com.pixelstack.ims.dto.AlbumDTO;
import com.pixelstack.ims.dto.CreateAlbumRequest;
import com.pixelstack.ims.dto.PageResult;
import com.pixelstack.ims.service.AlbumService;
import com.pixelstack.ims.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public Result<AlbumDTO> createAlbum(@Valid @RequestBody CreateAlbumRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        AlbumDTO album = albumService.createAlbum(request, userId);
        return Result.success(album);
    }

    @GetMapping("/page")
    public Result<PageResult<AlbumDTO>> getAlbumPage(@RequestParam(defaultValue = "1") Integer current,
                                                    @RequestParam(defaultValue = "50") Integer size,
                                                    @RequestParam(required = false) Long categoryId,
                                                    @RequestParam(required = false) String keyword,
                                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<AlbumDTO> result = albumService.getAlbumPage(current, size, categoryId, keyword, userId);
        return Result.success(result);
    }

    @GetMapping("/stared/page")
    public Result<PageResult<AlbumDTO>> getStaredAlbumPage(@RequestParam(defaultValue = "1") Integer current,
                                                          @RequestParam(defaultValue = "50") Integer size,
                                                          HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<AlbumDTO> result = albumService.getStaredAlbumPage(current, size, userId);
        return Result.success(result);
    }

    @GetMapping("/{albumId}")
    public Result<AlbumDTO> getAlbumDetail(@PathVariable Long albumId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        AlbumDTO album = albumService.getAlbumDetail(albumId, userId);
        return Result.success(album);
    }

    @PostMapping("/{albumId}/images")
    public Result<Void> addImageToAlbum(@PathVariable Long albumId,
                                       @RequestBody List<Long> imageIds,
                                       HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        albumService.addImageToAlbum(albumId, imageIds, userId);
        return Result.success();
    }

    @DeleteMapping("/{albumId}/images/{imageId}")
    public Result<Void> removeImageFromAlbum(@PathVariable Long albumId,
                                            @PathVariable Long imageId,
                                            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        albumService.removeImageFromAlbum(albumId, imageId, userId);
        return Result.success();
    }

    @PostMapping("/{albumId}/star")
    public Result<Void> starAlbum(@PathVariable Long albumId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        albumService.starAlbum(albumId, userId);
        return Result.success();
    }

    @DeleteMapping("/{albumId}/star")
    public Result<Void> unstarAlbum(@PathVariable Long albumId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        albumService.unstarAlbum(albumId, userId);
        return Result.success();
    }

    @DeleteMapping("/{albumId}")
    public Result<Void> deleteAlbum(@PathVariable Long albumId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        albumService.deleteAlbum(albumId, userId);
        return Result.success();
    }
}