package com.pixelstack.ims.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageInfo {
    private Integer iid;
    private String url;
    private Integer count;
    private Integer star;
    private Integer thumb;

    public ImageInfo() {}

    public ImageInfo(Integer iid, String url, Integer count) {
        this.iid = iid;
        this.url = url;
        this.count = count;
    }

    public ImageInfo(Integer iid, String url, Integer count, Integer star, Integer thumb) {
        this.iid = iid;
        this.url = url;
        this.count = count;
        this.star = star;
        this.thumb = thumb;
    }
}