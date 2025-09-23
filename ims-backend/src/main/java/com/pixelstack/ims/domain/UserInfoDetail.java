package com.pixelstack.ims.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDetail {
    private String username;
    private String email;
    private String introduction;
    private String authority;
    private Integer fans;
    private Integer follow;
    private Integer star;
    private Integer like;

    public UserInfoDetail() {}

    public UserInfoDetail(String username, String email, String introduction, String authority,
                         Integer fans, Integer follow, Integer star, Integer like) {
        this.username = username;
        this.email = email;
        this.introduction = introduction;
        this.authority = authority;
        this.fans = fans;
        this.follow = follow;
        this.star = star;
        this.like = like;
    }
}