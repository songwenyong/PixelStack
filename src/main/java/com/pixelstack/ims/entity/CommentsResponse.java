package com.pixelstack.ims.entity;

import com.pixelstack.ims.domain.CommentInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentsResponse {
    private List<CommentInfo> comments;

    public CommentsResponse() {}

    public CommentsResponse(List<CommentInfo> comments) {
        this.comments = comments;
    }
}