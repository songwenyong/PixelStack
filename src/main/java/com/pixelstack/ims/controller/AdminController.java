package com.pixelstack.ims.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pixelstack.ims.common.Auth.UserLoginToken;
import com.pixelstack.ims.domain.User;
import com.pixelstack.ims.entity.ApiResponse;
import com.pixelstack.ims.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/admin")
public class AdminController {


    @Autowired
    AdminService adminService;

    @JsonView(User.UserSimpleView.class)
    @UserLoginToken
    @GetMapping(value = {"/getUserListByPage"})
    public ApiResponse<Map<String, Object>> getUserListByPage(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize, int type) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo<User> pageInfo = null;
        if (type == 0)
            pageInfo = new PageInfo<>(adminService.getUserList());   // 查询用户
        else if (type == 1)
            pageInfo = new PageInfo<>(adminService.getAdminList());  // 查询管理员

        Map<String, Object> data = new HashMap<>();
        data.put("userList", pageInfo.getList());

        return ApiResponse.success(data)
                .withPagination(pageInfo.getTotal(), pageInfo.getPageNum(),
                               pageInfo.getPrePage(), pageInfo.getNextPage(),
                               pageInfo.getNavigateLastPage());
    }

    @JsonView(User.UserSimpleView.class)
    @UserLoginToken
    @GetMapping(value = {"/getUserList"})
    public ApiResponse<Map<String, Object>> getUserList(int type) {
        List<User> userList = null;
        if (type == 0)
            userList = adminService.getUserList();
        else if (type == 1)
            userList = adminService.getAdminList();

        Map<String, Object> data = new HashMap<>();
        data.put("userList", userList);
        return ApiResponse.success(data);
    }


    @UserLoginToken
    @PostMapping(value = {"/manageCountStatus"})
    public ApiResponse<Void> manageCountStatus(@RequestBody JSONObject jsonObject) {
        if (adminService.updateState(jsonObject)) {
            return ApiResponse.success("修改状态成功");
        }
        else {
            return ApiResponse.error("修改状态失败");
        }
    }

    @UserLoginToken
    @PostMapping(value = {"/createCount"})  // 由前端传回权限值是不安全的
    public ApiResponse<Void> createCount(User user, String authority) throws  InterruptedException{
        int status = adminService.createCount(user, authority);
        if (status == 0) {
            return ApiResponse.error("权限不足，创建失败");
        }
        else {
            return ApiResponse.success("创建成功");
        }
    }

    @UserLoginToken
    @PostMapping(value = {"/dealWithReport"})
    public ApiResponse<Void> dealWithReport(int cid, boolean reportRight) {
        if (adminService.dealWithReport(cid, reportRight)) {
            return ApiResponse.success("处理成功");
        }
        else {
            return ApiResponse.error("处理失败");
        }
    }
}