package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 设置允许的源，使用具体的域名而不是通配符
        corsConfiguration.addAllowedOrigin("http://8.130.169.157");
        corsConfiguration.addAllowedOrigin("http://localhost:5173"); // 开发环境

        // 允许携带认证信息（cookies等）
        corsConfiguration.setAllowCredentials(true);

        // 允许的请求头
        corsConfiguration.addAllowedHeader("*");

        // 允许的请求方法
        corsConfiguration.addAllowedMethod("*");

        // 允许暴露的响应头
        corsConfiguration.addExposedHeader("*");

        // 预检请求的有效期，单位为秒
        corsConfiguration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}