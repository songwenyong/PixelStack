package com.pixelstack.ims.mapper;

import org.apache.ibatis.annotations.Param;

public interface ThumbMapper {

    int getThumbByiid(int iid);

    int addThumbByUid(@Param("iid") int iid, @Param("uid") int uid);

    int deleteThumbByUid(@Param("iid") int iid, @Param("uid") int uid);

    int checkThumbByUid(@Param("iid") int iid, @Param("uid") int uid);

}
