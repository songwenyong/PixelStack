package com.pixelstack.ims.common.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径生效
                .allowedOrigins("http://localhost:8080", "http://localhost:8081") // Spring Framework 5.3 之前用法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // 允许的方法
                .allowedHeaders("*") // 允许的头部
                .allowCredentials(true) // 允许凭证
                .maxAge(3600); // 预检请求缓存时间:cite[4]:cite[5]:cite[6]
    }
}