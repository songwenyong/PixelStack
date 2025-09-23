package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private Integer uid;
    private String username;
    private String email;
    private String introduction;
    private String status;
    private String authority;
    private String token;

    public UserInfoResponse() {}

    public UserInfoResponse(Integer uid, String username, String email, String introduction, String status, String authority, String token) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.introduction = introduction;
        this.status = status;
        this.authority = authority;
        this.token = token;
    }
}