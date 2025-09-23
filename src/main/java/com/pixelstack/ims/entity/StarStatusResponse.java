package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StarStatusResponse {
    private Boolean isStar;

    public StarStatusResponse() {}

    public StarStatusResponse(Boolean isStar) {
        this.isStar = isStar;
    }
}