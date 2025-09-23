package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThumbStatusResponse {
    private Boolean isThumb;

    public ThumbStatusResponse() {}

    public ThumbStatusResponse(Boolean isThumb) {
        this.isThumb = isThumb;
    }
}