package com.pixelstack.ims.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredential {
    private Integer uid;
    private String password;

    public UserCredential() {}

    public UserCredential(Integer uid, String password) {
        this.uid = uid;
        this.password = password;
    }
}