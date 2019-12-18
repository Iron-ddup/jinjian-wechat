package com.jumian.wechat.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    private static ThreadLocal<ApiResponse> responseThreadLocal = new ThreadLocal<>();

    public static ApiResponse buildResult() {
        ApiResponse response = new ApiResponse();

        responseThreadLocal.set(response);

        return response;
    }

    public static ApiResponse buildResult(Object data) {
        ApiResponse response = new ApiResponse();


        if (data == null) {
            response.setMessage("找不到数据");
        } else {
            response.setData(data);
        }
        responseThreadLocal.set(response);

        return response;
    }

    public static ApiResponse buildResult(boolean isSuccess) {
        ApiResponse response = null;
        if (isSuccess) {
            response = new ApiResponse(0, "操作成功");
        } else {
            response = new ApiResponse(1, "操作失败");
        }
        responseThreadLocal.set(response);

        return response;
    }

    public static ApiResponse buildResult(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        ApiResponse response = new ApiResponse();
        response.setData(map);
        responseThreadLocal.set(response);

        return response;
    }


    public static ApiResponse buildResult(Integer code, String message) {
        ApiResponse response = new ApiResponse(code, message);
        responseThreadLocal.set(response);

        return response;
    }

    public static ApiResponse buildResult(Integer code, String message, Object data) {
        ApiResponse response = new ApiResponse(code, message);

        if (data == null) {
            response.setMessage("找不到数据");
        } else {
            response.setData(data);
        }

        responseThreadLocal.set(response);

        return response;
    }

    public static ApiResponse getResult() {
        return responseThreadLocal.get();
    }

}