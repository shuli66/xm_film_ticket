package com.example.common.enums;

public enum ResultCodeEnum {

    SUCCESS("200", "成功"),
    FAIL("201", "失败"),
    PARAM_ERROR("202", "参数不正确"),
    SERVICE_ERROR("203", "服务异常"),
    DATA_ERROR("204", "数据异常"),
    DATA_UPDATE_ERROR("205", "数据版本异常"),
    
    LOGIN_AUTH("208", "未登陆"),
    PERMISSION_ERROR("209", "没有权限"),
    
    CODE_ERROR("210", "验证码错误"),
    LOGIN_DISABLED_ERROR("212", "该用户已被禁用"),
    REGISTER_MOBLE_ERROR("213", "手机号已被使用"),
    LOGIN_AURH_ERROR("214", "需要登录"),
    LOGIN_ACL_ERROR("215", "没有权限"),
    
    URL_ENCODE_ERROR("216", "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR("217", "非法回调请求"),
    FETCH_ACCESSTOKEN_FAILD("218", "获取accessToken失败"),
    FETCH_USERINFO_ERROR("219", "获取用户信息失败"),
    
    TOKEN_ERROR("401", "token不合法"),
    TOKEN_CHECK_ERROR("402", "token验证失败"),
    TOKEN_INVALID_ERROR("403", "token已失效"),
    
    USER_EXIST_ERROR("5001", "用户已存在"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在或密码错误"),
    USER_ACCOUNT_LOCKED("5006", "账号已被锁定"),
    USER_ACCOUNT_INVALID("5007", "账号已作废"),
    USER_ACCOUNT_NOT_EXIST("5008", "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST("5009", "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS("5010", "账号下线，已经在其他设备登录"),
    USER_REGISTER_ERROR("5011", "注册失败"),
    USER_REGISTER_PARAM_EMPTY("5012", "注册参数为空"),
    USER_REGISTER_PARAM_ERROR("5013", "注册参数错误"),
    USER_REGISTER_PASSWORD_ERROR("5014", "密码格式错误"),
    USER_REGISTER_MOBILE_ERROR("5015", "手机号格式错误"),
    USER_REGISTER_MOBILE_EXIST("5016", "手机号已被注册"),
    USER_REGISTER_USERNAME_EXIST("5017", "用户名已被注册"),
    
    SYSTEM_ERROR("9999", "系统异常"),
    
    PAY_RUN("220", "支付中"),
    CANCEL_ORDER_FAIL("225", "取消订单失败"),
    CANCEL_ORDER_NO("225", "不能取消预约"),

    
    SIGN_ERROR("300", "签名错误"),
    USER_PERMISSION_ERROR("5005", "用户权限不足"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
