package com.pixelstack.ims.mapper;

import com.pixelstack.ims.domain.Comment;
import com.pixelstack.ims.domain.CommentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {

    List<CommentInfo> getCommentByiid(@Param("iid") int iid);

    int addComment(Comment comment);

    int addCommentRelate(@Param("iid") int iid, @Param("cid") int cid);

    int updateReport(@Param("report") boolean report, @Param("cid") int cid);

    int deleteComment(@Param("cid") int cid);

    List<CommentInfo> getCommentWithReport();

}
