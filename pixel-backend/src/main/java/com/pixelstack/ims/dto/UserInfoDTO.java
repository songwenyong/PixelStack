package com.pixelstack.ims.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoDTO {
    private Long id;
    private String username;
    private Integer roleType;
    private String email;
    private String phone;
    private String introduction;
    private LocalDateTime createdAt;
}