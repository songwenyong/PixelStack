package com.pixelstack.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pixelstack.ims.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}