package com.pixelstack.ims.entity;

import com.pixelstack.ims.domain.ImageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StarListResponse {
    private List<ImageInfo> starList;

    public StarListResponse() {}

    public StarListResponse(List<ImageInfo> starList) {
        this.starList = starList;
    }
}