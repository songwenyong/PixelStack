package com.pixelstack.ims.mapper;

import com.pixelstack.ims.domain.Tag;
import org.apache.ibatis.annotations.Param;

public interface TagMapper {

    Tag selectTagByName(String tagname);

    int addTagByName(Tag tag);

    int deleteTagById(int tid);

    int addTagsRelate(@Param("iid") int iid, @Param("tid") int tid);

}
