package com.example.exception;

import com.example.common.enums.ResultCodeEnum;
import lombok.Getter;
@Getter
public class CustomException extends RuntimeException {

    private String code;
    private String msg;

    public CustomException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.msg);
        this.code = resultCodeEnum.code;
    }

    public CustomException(ResultCodeEnum resultCodeEnum, String msg) {
        super(msg);
        this.code = resultCodeEnum.code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
