package com.pixelstack.ims.controller;

import com.alibaba.fastjson.JSONObject;
import com.pixelstack.ims.common.Auth.Authentication;
import com.pixelstack.ims.common.Auth.UserLoginToken;
import com.pixelstack.ims.common.exception.Result_Error;
import com.pixelstack.ims.common.exception.InternalErrorException;
import com.pixelstack.ims.common.exception.NotFoundException;
import com.pixelstack.ims.domain.Tag;
import com.pixelstack.ims.domain.User;
import com.pixelstack.ims.domain.UserInfo;
import com.pixelstack.ims.domain.UserInfoDetail;
import com.pixelstack.ims.entity.*;

import com.pixelstack.ims.service.GeneralService;
import com.pixelstack.ims.service.ImageService;
import com.pixelstack.ims.service.TagService;
import com.pixelstack.ims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value="/user")     // 通过这里配置使下面的映射都在 /user 下
public class UserController {

    public final static int ERROR = 0;

    @Autowired
    UserService userService;

    @Autowired
    Authentication authentication;

    @Autowired
    ImageService imageService;

    @Autowired
    GeneralService generalService;

    @Autowired
    TagService tagService;

    @ResponseBody
    @PostMapping(value = {"/register"})
    public ApiResponse<Void> userRegister(User user) throws InternalErrorException {
        if (user.getPassword() == null || user.getUsername() == null) {
            return ApiResponse.error("用户名或者密码为空");
        }
        int status = userService.register(user, "user");
        if (status == ERROR) {
            // 注册出现错误，抛出错误
            throw new InternalErrorException("username or password null", Result_Error.ErrorCode.INSERT_ERROR.getCode());
        }
        else {
            return ApiResponse.success("注册成功");
        }
    }


    @PostMapping(value = {"/login"})
    public ApiResponse<UserInfoResponse> checkUser(User user) throws NotFoundException{
        User login_user = userService.login(user);
        if (login_user == null) {
            return ApiResponse.errorTyped("用户不存在或者密码不正确");
        }
        else if (login_user.getStatus().equals("frozen")) {
            return ApiResponse.errorTyped("账户被冻结90天");
        }
        else if (login_user.getStatus().equals("terminate")) {
            return ApiResponse.errorTyped("账户被封停");
        }
        else {
            // 登陆成功，创建 token 并保存至 redis 中
            String token = authentication.createToken(login_user);
            if (token != null && authentication.storeToken(token, login_user.getUid())) {
                UserInfoResponse userInfo = new UserInfoResponse(
                    login_user.getUid(),
                    login_user.getUsername(),
                    login_user.getEmail(),
                    login_user.getIntroduction(),
                    login_user.getStatus(),
                    login_user.getAuthority(),
                    token
                );
                return ApiResponse.success(userInfo);
            }
            else {
                return ApiResponse.errorTyped("token 创建或设置失败，请稍后重试");
            }
        }
    }


    @UserLoginToken
    @GetMapping(value = {"/getUserInfo"})
    public ApiResponse<UserInfoDetail> getUserInfo(int uid) {
        UserInfoDetail userInfo = userService.getUserInfo(uid);
        if (userInfo == null) {
            return ApiResponse.errorTyped("无此用户");
        }
        else {
            return ApiResponse.success(userInfo);
        }
    }

    @UserLoginToken
    @PostMapping(value = {"/modify"})
    public ApiResponse<Void> modify(User user) {
        int status = userService.modify(user);
        if (status == 0) {
            return ApiResponse.error("信息修改不规范");
        }
        else {
            if (status == 2) {
                return new ApiResponse<>(201, "密码已修改，请重新登陆");
            }
            else {
                return ApiResponse.success("修改成功");
            }
        }
    }

    @UserLoginToken
    @PostMapping(value = {"/upload"})
    public ApiResponse<UploadResponse> upload(@RequestParam("file") MultipartFile[] files, int uid, String title) throws IOException {
        if (files == null || files.length == 0) {
            return ApiResponse.errorTyped("前上传照片");
        }
        UserInfoDetail userDetail = userService.getUserInfo(uid);
        String username = userDetail != null ? userDetail.getUsername() : null;
        if (username == null) {
            return ApiResponse.errorTyped("用户不存在");
        }
        else {
            HashMap imageInfo = (HashMap) imageService.upload(files, username, title);
            ArrayList postList = (ArrayList) imageInfo.get("postList");
            ArrayList errorList  = (ArrayList) imageInfo.get("errorList");

            UploadResponse uploadResponse = new UploadResponse(
                postList,
                errorList,
                postList != null ? postList.size() : 0,
                errorList != null ? errorList.size() : 0
            );

            return ApiResponse.success(uploadResponse);
        }
    }

    @UserLoginToken
    @PostMapping(value = {"/deleteImage"})
    public ApiResponse<Void> deleteImage(int iid) {
        if (imageService.deleteImageByiid(iid)) {
            return ApiResponse.success("删除成功");
        } else {
            return ApiResponse.error("删除失败");
        }
    }

    @UserLoginToken
    @PostMapping(value = {"/addTagsandTitle"})
    public ApiResponse<Void> addTags(@RequestBody JSONObject jsonObject) throws IOException{
        ArrayList tags = (ArrayList) jsonObject.get("tags");
        ArrayList pids = (ArrayList) jsonObject.get("pids");
        if (pids == null || tags == null) {
            return ApiResponse.error("Tags or pids are null");
        }
        else if (generalService.addTags(tags, pids)) {
            return ApiResponse.success("添加标签成功");
        } else {
            return ApiResponse.error("添加标签失败");
        }
    }

    @GetMapping(value = {"/getUid"})
    public ApiResponse<UidResponse> getUidByUsername(String username) {
        Integer uid = userService.getUidByUsername(username);
        if (uid == null) {
            return ApiResponse.errorTyped("查无此人");
        }
        else {
            UidResponse uidResponse = new UidResponse(uid);
            return ApiResponse.success(uidResponse);
        }
    }

    @UserLoginToken
    @PostMapping(value = {"/isFollow"})
    public ApiResponse<FollowStatusResponse> isFollow(int uid, int fid, boolean isFollow) {
        if (generalService.followOther(uid, fid, isFollow)) {
            FollowStatusResponse response = new FollowStatusResponse(isFollow);
            return ApiResponse.success(response);
        }
        else {
            return ApiResponse.errorTyped("error");
        }
    }

    @PostMapping(value = {"/followRelate"})
    public ApiResponse<FollowStatusResponse> followRelate(@RequestParam(defaultValue = "0") int uid, @RequestParam(defaultValue = "0") int fid) {
        boolean isFollow = false;
        if (fid != 0 && generalService.Isfollow(uid, fid))
            isFollow = true;
        FollowStatusResponse response = new FollowStatusResponse(isFollow);
        return ApiResponse.success(response);
    }

    @UserLoginToken
    @GetMapping(value = {"getFollowers"})
    public ApiResponse<FollowersResponse> getFollowers(int uid) {
        List<UserInfo> followers = generalService.getFollowers(uid);
        if (followers == null) {
            return ApiResponse.errorTyped("error");
        }
        else {
            FollowersResponse response = new FollowersResponse(followers);
            return ApiResponse.success(response);
        }
    }

    @UserLoginToken
    @GetMapping(value = {"getFans"})
    public ApiResponse<FansResponse> getFans(int uid) {
        List<UserInfo> fans = generalService.getFans(uid);
        if (fans == null) {
            return ApiResponse.errorTyped("maybe no fans");
        }
        else {
            FansResponse response = new FansResponse(fans);
            return ApiResponse.success(response);
        }
    }


}
