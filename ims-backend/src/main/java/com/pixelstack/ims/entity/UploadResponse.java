package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UploadResponse {
    private List<Object> upload;
    private List<Object> errors;
    private Integer success;
    private Integer fail;

    public UploadResponse() {}

    public UploadResponse(List<Object> upload, List<Object> errors, Integer success, Integer fail) {
        this.upload = upload;
        this.errors = errors;
        this.success = success;
        this.fail = fail;
    }
}