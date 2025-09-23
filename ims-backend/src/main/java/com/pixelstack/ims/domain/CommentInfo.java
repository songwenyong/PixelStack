package com.pixelstack.ims.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentInfo {
    private Integer cid;
    private String username;
    private String content;
    private Date cdate;
    private Boolean report;

    public CommentInfo() {}

    public CommentInfo(Integer cid, String username, String content, Date cdate) {
        this.cid = cid;
        this.username = username;
        this.content = content;
        this.cdate = cdate;
    }

    public CommentInfo(Integer cid, String username, String content, Date cdate, Boolean report) {
        this.cid = cid;
        this.username = username;
        this.content = content;
        this.cdate = cdate;
        this.report = report;
    }
}