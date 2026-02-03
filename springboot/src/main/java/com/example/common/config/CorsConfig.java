package com.example.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    /**
     * 从配置文件读取允许的跨域源（逗号分隔）
     * 在 application.yml 或 application-docker.yml 中配置:
     * cors:
     *   allowed-origins: http://localhost:5173,http://your-server-ip:port
     */
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 从配置文件读取允许的源（逗号分隔）
        Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .forEach(corsConfiguration::addAllowedOrigin);

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