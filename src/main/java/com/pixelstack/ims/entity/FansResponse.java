package com.pixelstack.ims.entity;

import com.pixelstack.ims.domain.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FansResponse {
    private List<UserInfo> fans;

    public FansResponse() {}

    public FansResponse(List<UserInfo> fans) {
        this.fans = fans;
    }
}