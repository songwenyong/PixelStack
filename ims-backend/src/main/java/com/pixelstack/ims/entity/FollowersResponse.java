package com.pixelstack.ims.entity;

import com.pixelstack.ims.domain.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FollowersResponse {
    private List<UserInfo> followers;

    public FollowersResponse() {}

    public FollowersResponse(List<UserInfo> followers) {
        this.followers = followers;
    }
}