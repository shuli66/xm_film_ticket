package com.example.common.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记无需Token验证的接口
 * 在Controller方法上添加此注解，可跳过Token验证
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PassToken {
    /**
     * 是否必须跳过验证，默认为true
     */
    boolean required() default true;
}
