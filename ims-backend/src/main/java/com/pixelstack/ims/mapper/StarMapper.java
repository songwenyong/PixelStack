package com.pixelstack.ims.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StarMapper {

    int getStarByiid(int iid);

    int addStarByUid(@Param("iid") int iid, @Param("uid") int uid);

    int deleteStarByUid(@Param("iid") int iid, @Param("uid") int uid);

    int checkStarByUid(@Param("iid") int iid, @Param("uid") int uid);

    List<Integer> getStars(int uid);

}
