package com.pixelstack.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pixelstack.ims.dto.LoginRequest;
import com.pixelstack.ims.dto.LoginResponse;
import com.pixelstack.ims.dto.RegisterRequest;
import com.pixelstack.ims.dto.UserInfoDTO;
import com.pixelstack.ims.entity.UserInfo;
import com.pixelstack.ims.exception.BusinessException;
import com.pixelstack.ims.mapper.UserInfoMapper;
import com.pixelstack.ims.service.UserService;
import com.pixelstack.ims.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserInfoMapper userInfoMapper;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUsername, request.getUsername());
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);

        if (userInfo == null || !userInfo.getPassword().equals(request.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(userInfo.getId(), userInfo.getUsername());

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userInfo, userInfoDTO);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserInfo(userInfoDTO);
        return response;
    }

    @Override
    public void register(RegisterRequest request) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUsername, request.getUsername());
        if (userInfoMapper.selectOne(wrapper) != null) {
            throw new BusinessException("用户名已存在");
        }

        wrapper.clear();
        wrapper.eq(UserInfo::getEmail, request.getEmail());
        if (userInfoMapper.selectOne(wrapper) != null) {
            throw new BusinessException("邮箱已被注册");
        }

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(request, userInfo);
        userInfo.setRoleType(1);
        userInfo.setCreator(0L);
        userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfoDTO getCurrentUser(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userInfo, userInfoDTO);
        return userInfoDTO;
    }

    @Override
    public UserInfoDTO updateUserInfo(Long userId, UserInfoDTO userInfoDTO) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }

        userInfo.setEmail(userInfoDTO.getEmail());
        userInfo.setPhone(userInfoDTO.getPhone());
        userInfo.setIntroduction(userInfoDTO.getIntroduction());
        userInfo.setUpdater(userId);

        userInfoMapper.updateById(userInfo);
        return getCurrentUser(userId);
    }
}