package com.pixelstack.ims.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiPageResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private Long total;
    private Integer curPage;
    private Integer prePage;
    private Integer nextPage;
    private Integer lastPage;

    public ApiPageResponse() {}

    public ApiPageResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiPageResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiPageResponse(Integer status, String message, T data, Long total, Integer curPage, Integer prePage, Integer nextPage, Integer lastPage) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.total = total;
        this.curPage = curPage;
        this.prePage = prePage;
        this.nextPage = nextPage;
        this.lastPage = lastPage;
    }

    public static <T> ApiPageResponse<T> success(T data, Long total, Integer curPage, Integer prePage, Integer nextPage, Integer lastPage) {
        return new ApiPageResponse<>(200, "success", data, total, curPage, prePage, nextPage, lastPage);
    }

    public static <T> ApiPageResponse<T> success(String message, T data, Long total, Integer curPage, Integer prePage, Integer nextPage, Integer lastPage) {
        return new ApiPageResponse<>(200, message, data, total, curPage, prePage, nextPage, lastPage);
    }

    public static ApiPageResponse<Void> error(String message) {
        return new ApiPageResponse<>(500, message);
    }

    public static <T> ApiPageResponse<T> error(String message, T data) {
        return new ApiPageResponse<>(500, message, data);
    }
}