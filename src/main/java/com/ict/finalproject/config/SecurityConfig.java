package com.ict.finalproject.config;

import com.ict.finalproject.DAO.TAdminDAO;

import com.ict.finalproject.JWT.JWTFilter;
import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.JWT.LoginFilter;
import com.ict.finalproject.Service.TAdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TAdminService tAdminService;  // TAdminDAO 주입
    private final JWTUtil jwtUtil;

    public SecurityConfig(TAdminService tAdminService, JWTUtil jwtUtil) {
        this.tAdminService = tAdminService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .formLogin(form -> form.disable())  // 기본 폼 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())  // HTTP 기본 인증 비활성화

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 관리 설정
                .logout(logout -> logout
                        .logoutUrl("/logoutOk")  // 로그아웃 요청을 처리할 URL
                        .logoutSuccessUrl("/")  // 로그아웃 성공 후 리다이렉트할 URL
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID")  // 세션 쿠키 삭제
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("접근 거부됨: " + authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "권한 없음");  // UNAUTHORIZED 응답
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:9911");  // 허용할 출처
        configuration.addAllowedHeader("*");  // 모든 헤더 허용
        configuration.addAllowedMethod("*");  // 모든 메서드 허용
        configuration.setAllowCredentials(true);  // 쿠키와 인증 정보 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCryptPasswordEncoder를 빈으로 등록
    }
}