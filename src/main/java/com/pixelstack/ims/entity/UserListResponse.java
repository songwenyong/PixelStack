package com.pixelstack.ims.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.pixelstack.ims.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListResponse {
    @JsonView(User.UserSimpleView.class)
    private List<User> userList;

    public UserListResponse() {}

    public UserListResponse(List<User> userList) {
        this.userList = userList;
    }
}