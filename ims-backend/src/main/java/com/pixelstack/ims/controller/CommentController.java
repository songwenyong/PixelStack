package com.pixelstack.ims.controller;


import com.pixelstack.ims.common.Auth.UserLoginToken;
import com.pixelstack.ims.domain.Comment;
import com.pixelstack.ims.domain.CommentInfo;
import com.pixelstack.ims.entity.*;
import com.pixelstack.ims.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/Comment")     // 通过这里配置使下面的映射都在 /user 下
public class CommentController {

    @Autowired
    CommentService commentService;


    @ResponseBody
    @GetMapping(value = {"/getCommentsByiid"})
    public ApiResponse<CommentsResponse> getCommentsByiid(int iid) {
        List<CommentInfo> commentList = commentService.getCommentByiid(iid);
        CommentsResponse response = new CommentsResponse(commentList);
        return ApiResponse.success(response);
    }

    @UserLoginToken
    @ResponseBody
    @PostMapping(value = {"/add"})
    public ApiResponse<Void> add(int iid, int uid, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCdate(new Date());
        comment.setUid(uid);
        if (commentService.addComment(iid, uid, comment)) {
            return ApiResponse.success("评论成功");
        }
        else {
            return ApiResponse.error("评论失败");
        }
    }

    @UserLoginToken
    @ResponseBody
    @PostMapping(value = {"/report"})
    public ApiResponse<Void> report(int cid) {
        if (commentService.reportComment(cid, true)) {
            return ApiResponse.success("举报成功");
        }
        else {
            return ApiResponse.error("举报失败");
        }
    }

    @UserLoginToken
    @ResponseBody
    @GetMapping(value = {"/getReportComment"})
    public ApiResponse<CommentsResponse> getReportComment() {
        List<CommentInfo> comments = commentService.getCommentWithReport();
        if (comments == null) {
            return ApiResponse.errorTyped("查询失败");
        }
        else {
            CommentsResponse response = new CommentsResponse(comments);
            return ApiResponse.success(response);
        }
    }

}
