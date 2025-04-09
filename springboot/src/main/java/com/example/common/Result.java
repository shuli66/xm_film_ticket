package com.example.common;

import lombok.Data;

@Data
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public static Result success() {
        Result result = new Result<>();
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(data);
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setCode("500");
        result.setMsg("系统错误");
        return result;
    }

    public Result message(String msg) {
        this.setMsg(msg);
        return this;
    }

    public Result code(String code) {
        this.setCode(code);
        return this;
    }
}
