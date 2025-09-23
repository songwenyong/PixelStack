package com.pixelstack.ims.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private Integer uid;
    private Integer fid;
    private String username;
    private String introduction;

    public UserInfo() {}

    public UserInfo(Integer uid, String username, String introduction) {
        this.uid = uid;
        this.username = username;
        this.introduction = introduction;
    }

    public UserInfo(Integer fid, String username, String introduction, boolean isFollower) {
        this.fid = fid;
        this.username = username;
        this.introduction = introduction;
    }
}