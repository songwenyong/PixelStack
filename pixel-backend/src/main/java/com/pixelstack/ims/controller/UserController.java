package com.pixelstack.ims.controller;

import com.pixelstack.ims.dto.LoginRequest;
import com.pixelstack.ims.dto.LoginResponse;
import com.pixelstack.ims.dto.RegisterRequest;
import com.pixelstack.ims.dto.UserInfoDTO;
import com.pixelstack.ims.service.UserService;
import com.pixelstack.ims.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success(response);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success();
    }

    @GetMapping("/current")
    public Result<UserInfoDTO> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserInfoDTO userInfo = userService.getCurrentUser(userId);
        return Result.success(userInfo);
    }

    @PutMapping("/update")
    public Result<UserInfoDTO> updateUserInfo(@RequestBody UserInfoDTO userInfo, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserInfoDTO updatedUser = userService.updateUserInfo(userId, userInfo);
        return Result.success(updatedUser);
    }
}