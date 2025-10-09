package com.pixelstack.ims.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserInfoDTO userInfo;
}