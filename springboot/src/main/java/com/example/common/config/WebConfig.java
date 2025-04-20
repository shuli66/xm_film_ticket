package com.example.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JWTInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器，拦截所有请求，排除登录、注册和文件相关的请求
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求路径
                .excludePathPatterns("/alipay/notify", "/alipay/pay")
                .excludePathPatterns(  // 排除的路径
                        "/",
                        "/login",
                        "/register",
                        "/email/sendVerificationCode",  // 添加发送验证码接口
                        "/email/verifyCode",            // 添加验证验证码接口
                        "/files/**",
                        "/favicon.ico" // 可选：排除请求favicon.ico图标
                );
    }
}

