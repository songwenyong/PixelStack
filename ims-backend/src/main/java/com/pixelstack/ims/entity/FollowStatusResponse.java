package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowStatusResponse {
    private Boolean isFollow;

    public FollowStatusResponse() {}

    public FollowStatusResponse(Boolean isFollow) {
        this.isFollow = isFollow;
    }
}