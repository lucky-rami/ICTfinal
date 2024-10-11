package com.ict.finalproject.config;

import com.ict.finalproject.JWT.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JWTInterceptor jwtInterceptor;

    // JwtInterceptor를 생성자 주입 방식으로 주입받음
    @Autowired
    public WebConfig(JWTInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

  /*  @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // JwtInterceptor 등록
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/master/**");  // /master/** 경로에 대해 인터셉터 적용
    }*/

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9911") // 허용할 출처(도메인) 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)  // 쿠키와 인증 정보를 허용
                .maxAge(3600);
    }
}