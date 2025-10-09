package com.pixelstack.ims.service;

import com.pixelstack.ims.dto.LoginRequest;
import com.pixelstack.ims.dto.LoginResponse;
import com.pixelstack.ims.dto.RegisterRequest;
import com.pixelstack.ims.dto.UserInfoDTO;

public interface UserService {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);

    UserInfoDTO getCurrentUser(Long userId);

    UserInfoDTO updateUserInfo(Long userId, UserInfoDTO userInfo);
}