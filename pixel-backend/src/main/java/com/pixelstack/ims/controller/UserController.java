package com.pixelstack.ims.controller;

import com.pixelstack.ims.dto.LoginRequest;
import com.pixelstack.ims.dto.LoginResponse;
import com.pixelstack.ims.dto.RegisterRequest;
import com.pixelstack.ims.dto.UserInfoDTO;
import com.pixelstack.ims.service.UserService;
import com.pixelstack.ims.util.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("User login attempt: username={}", request.getUsername());
        LoginResponse response = userService.login(request);
        log.info("User login successful: username={}", request.getUsername());
        return Result.success(response);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        log.info("User registration attempt: username={}, email={}", request.getUsername(), request.getEmail());
        userService.register(request);
        log.info("User registration successful: username={}", request.getUsername());
        return Result.success();
    }

    @GetMapping("/current")
    public Result<UserInfoDTO> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.debug("Get current user info: userId={}", userId);
        UserInfoDTO userInfo = userService.getCurrentUser(userId);
        return Result.success(userInfo);
    }

    @PutMapping("/update")
    public Result<UserInfoDTO> updateUserInfo(@RequestBody UserInfoDTO userInfo, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("Update user info: userId={}", userId);
        UserInfoDTO updatedUser = userService.updateUserInfo(userId, userInfo);
        log.info("User info updated successfully: userId={}", userId);
        return Result.success(updatedUser);
    }
}