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
        corsConfiguration.addAllowedOrigin("*");  // 允许所有源
        corsConfiguration.addAllowedHeader("*");  // 允许所有请求头
        corsConfiguration.addAllowedMethod("*");  // 允许所有请求方法
        source.registerCorsConfiguration("/**", corsConfiguration);  // 对所有接口配置跨域
        return new CorsFilter(source);
    }
}