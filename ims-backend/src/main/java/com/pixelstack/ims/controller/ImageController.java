package com.pixelstack.ims.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.pixelstack.ims.common.Auth.UserLoginToken;
import com.pixelstack.ims.domain.ImageInfo;
import com.pixelstack.ims.entity.*;
import com.pixelstack.ims.service.GeneralService;
import com.pixelstack.ims.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping(value="/image")     // 通过这里配置使下面的映射都在 /user 下
public class ImageController {


    @Autowired
    ImageService imageService;

    @Autowired
    GeneralService generalService;

    @ResponseBody
    @GetMapping(value = {"/getImageDetails"})
    public ApiResponse<ImageDetailsResponse> getImageDetails(int iid, @RequestParam(defaultValue = "0") int uid) throws IOException {
        HashMap details = (HashMap) imageService.getImageDetailByiid(iid, uid);
        if (details == null) {
            return ApiResponse.errorTyped("图片不存在");
        }
        else {
            ImageDetailsResponse response = new ImageDetailsResponse(
                (String) details.get("title"),
                (String) details.get("author"),
                (String) details.get("upload"),
                (String) details.get("url"),
                (Integer) details.get("count"),
                (List<Object>) details.get("tags"),
                (Integer) details.get("star"),
                (Integer) details.get("thumb"),
                (Boolean) details.get("isStar"),
                (Boolean) details.get("isThumb"),
                (Boolean) details.get("isFollow")
            );
            return ApiResponse.success(response);
        }
    }

    @ResponseBody
    @GetMapping(value = {"/getImageList"})
    public ApiPageResponse<ImageListResponse> getImageList(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "60") int pageSize) {
        PageInfo<ImageInfo> pageInfo = imageService.getImageList(pageNo, pageSize);
        ImageListResponse response = new ImageListResponse(pageInfo.getList());
        return ApiPageResponse.success(response, pageInfo.getTotal(), pageInfo.getPageNum(),
                pageInfo.getPrePage(), pageInfo.getNextPage(),
                pageInfo.getNavigateLastPage());
    }

    @UserLoginToken
    @ResponseBody
    @GetMapping(value = {"/getImageListByUid"})
    public ApiResponse<ImageListResponse> getImageListByUid(int uid) {
        List<ImageInfo> imageList = imageService.getImageListByUid(uid);
        ImageListResponse response = new ImageListResponse(imageList);
        return ApiResponse.success(response);
    }


    @UserLoginToken
    @GetMapping(value = {"/isStar"})
    public ApiResponse<StarStatusResponse> isStar(int iid, int uid, boolean isStar) {
        if (generalService.IsStar(iid, uid, isStar)) {
            StarStatusResponse response = new StarStatusResponse(isStar);
            return ApiResponse.success(response);
        }
        else {
            return ApiResponse.errorTyped("error");
        }
    }

    @UserLoginToken
    @GetMapping(value = {"/isThumb"})
    public ApiResponse<ThumbStatusResponse> isThumb(int iid, int uid, boolean isThumb) {
        if (generalService.IsThumb(iid, uid, isThumb)) {
            ThumbStatusResponse response = new ThumbStatusResponse(isThumb);
            return ApiResponse.success(response);
        }
        else {
            return ApiResponse.errorTyped("error");
        }
    }

    @UserLoginToken
    @GetMapping(value = {"/myStars"})
    public ApiResponse<StarListResponse> myStars(int uid) {
        List<ImageInfo> myStars = imageService.getMyStars(uid);
        StarListResponse response = new StarListResponse(myStars);
        return ApiResponse.success(response);
    }

    @GetMapping(value = {"/getListBySearch"})
    public ApiResponse<ImageListResponse> getListByTagNameOrAuthorOrTitle(@RequestParam(defaultValue = "0") String type,
                                                  @RequestParam(defaultValue = "海豹") String search)
    {
        List<ImageInfo> imgs = imageService.getListByTagNameOrAuthorOrTitle(type, search);
        if (imgs == null) {
            return ApiResponse.errorTyped("搜索失败");
        } else {
            ImageListResponse response = new ImageListResponse(imgs);
            return ApiResponse.success(response);
        }
    }

    @UserLoginToken
    @PostMapping(value = "/updateTitle")
    public ApiResponse<Void> updateTitle(int iid, String title) {
        if (title == null || title.equals("") || !imageService.updateTitle(iid, title)) {
            return ApiResponse.error("修改无效");
        }
        else {
            return ApiResponse.success("修改完成");
        }
    }
}
