package com.mobplus.greenspeed.util;

/**
 * @author: Leonard
 * @create: 2021/9/13
 */

import com.alibaba.fastjson.JSON;
import com.yeecloud.meeto.common.result.ResultCode;

public class Result<T> {
    private int code;
    private String message;
    private T result;

    public Result() {
    }

    public Result(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    public static <T> Result SUCCESS() {
        return SUCCESS((Object)null);
    }

    public static <T> Result SUCCESS(T result) {
        return new Result(2000, "ok", result);
    }

    public static Result FAILURE(String message) {
        return FAILURE(5000, message);
    }

    public static Result FAILURE(int code, String message) {
        return new Result(code, message, (Object)null);
    }

    public static Result FAILURE(ResultCode result) {
        return new Result(result.getCode(), result.getMessage(), (Object)null);
    }
}
