package com.pixelstack.ims.mapper;

import com.pixelstack.ims.domain.User;
import com.pixelstack.ims.domain.UserInfo;
import com.pixelstack.ims.domain.UserCredential;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    int addUser(User user);

    User checkUser(User user);

    User selectUserById(@Param("uid") int uid);

    int updateUserById(User user);

    int getStarCount(@Param("uid") int uid);

    int getThumbCount(@Param("uid") int uid);

    int getFansCount(@Param("fid") int fid);

    int getFollowCount(@Param("uid") int uid);

    List<User> getAllUser();

    int follow(@Param("uid") int uid, @Param("fid") int fid);

    int unfollow(@Param("uid") int uid, @Param("fid") int fid);

    int checkFollowByFid(@Param("uid") int uid, @Param("fid") int fid);

    Integer getUidByUsername(String username);

    List<UserInfo> getFollowers(int uid);

    List<UserInfo> getFans(int uid);

    List<UserCredential> getUsersByuids(@Param("list") List<String> list);

    int unBlock();

}
