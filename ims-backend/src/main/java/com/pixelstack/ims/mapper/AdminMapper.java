package com.pixelstack.ims.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.pixelstack.ims.domain.User;

import java.util.List;

public interface AdminMapper extends  UserMapper{

    Page<User> getUserList();

    Page<User> getAdminList();

    int updateStateById(JSONObject jsonObject);

    List<User> getAllAdmin();

}
