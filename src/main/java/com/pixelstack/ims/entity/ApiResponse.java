package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private Long total;
    private Integer curPage;
    private Integer prePage;
    private Integer nextPage;
    private Integer lastPage;

    public ApiResponse() {}

    public ApiResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(200, message);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(500, message);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(500, message, data);
    }

    public static <T> ApiResponse<T> errorTyped(String message) {
        return new ApiResponse<>(500, message);
    }

    public ApiResponse<T> withPagination(Long total, Integer curPage, Integer prePage, Integer nextPage, Integer lastPage) {
        this.total = total;
        this.curPage = curPage;
        this.prePage = prePage;
        this.nextPage = nextPage;
        this.lastPage = lastPage;
        return this;
    }
}