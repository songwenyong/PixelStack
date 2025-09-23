package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImageDetailsResponse {
    private String title;
    private String author;
    private String upload;
    private String url;
    private Integer count;
    private List<Object> tags;
    private Integer star;
    private Integer thumb;
    private Boolean isStar;
    private Boolean isThumb;
    private Boolean isFollow;

    public ImageDetailsResponse() {}

    public ImageDetailsResponse(String title, String author, String upload, String url, Integer count,
                               List<Object> tags, Integer star, Integer thumb, Boolean isStar, Boolean isThumb, Boolean isFollow) {
        this.title = title;
        this.author = author;
        this.upload = upload;
        this.url = url;
        this.count = count;
        this.tags = tags;
        this.star = star;
        this.thumb = thumb;
        this.isStar = isStar;
        this.isThumb = isThumb;
        this.isFollow = isFollow;
    }
}