package com.pixelstack.ims.entity;

import com.pixelstack.ims.domain.ImageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImageListResponse {
    private List<ImageInfo> imageList;

    public ImageListResponse() {}

    public ImageListResponse(List<ImageInfo> imageList) {
        this.imageList = imageList;
    }
}