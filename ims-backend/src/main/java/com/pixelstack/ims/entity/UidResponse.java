package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UidResponse {
    private Integer uid;

    public UidResponse() {}

    public UidResponse(Integer uid) {
        this.uid = uid;
    }
}