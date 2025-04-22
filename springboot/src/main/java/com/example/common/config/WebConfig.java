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
                .excludePathPatterns(  // 排除的路径
                        "/",
                        "/login",
                        "/register",
                        "/email/sendVerificationCode",  // 邮箱验证码
                        "/email/verifyCode",            // 验证邮箱验证码
                        "/user/resetPassword",          // 重置密码
                        "/user/checkUsername",          // 检查用户名
                        "/user/checkPhone",             // 检查手机号
                        "/user/sendCode",               // 发送短信验证码
                        "/map/**",                      // 地图相关接口
                        "/alipay/notify",               // 支付宝回调
                        "/alipay/pay",                  // 支付宝支付
                        "/files/**",                    // 静态文件
                        "/favicon.ico"                  // 图标文件
                );
    }
}

