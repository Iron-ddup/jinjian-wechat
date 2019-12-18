package com.jumian.wechat.utils;

import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 通用响应
 *
 * @param <T>
 */
public class ApiResponse<T> implements Serializable {

    /**
     * 0:success
     * 非0：异常
     */
    private int code;
    private String message = "成功";
    private String traceId;
    private T data;

    public ApiResponse() {
        this.traceId = MDC.get("X-B3-TraceId");
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.traceId = MDC.get("X-B3-TraceId");
    }

    public static ApiResponse invalidResponse(String message) {
        return new ApiResponse(-1, message);
    }

    public static <T> ApiResponse<T> successResponse(T data) {
        ApiResponse resp = new ApiResponse(0, "success");
        resp.setData(data);
        return resp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
