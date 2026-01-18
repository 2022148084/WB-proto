package com.writingboard.server.global.config;

import com.writingboard.server.global.auth.jwt.JwtFilter;
import com.writingboard.server.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    // 비밀번호 암호화 기계 등록 (필수)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API 서버라 CSRF 필요 없음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 안 씀 (JWT니까)
                .authorizeHttpRequests(auth -> auth
                        // 1. 문 열어줄 곳 (회원가입, 로그인, 웹소켓 연결, html, js 등)
                        .requestMatchers(
                                "/api/members/join",
                                "/api/members/login",
                                "/ws/**",
                                "/ws-stomp/**",
                                "/", "/*.html", "/*.js", "/*.css" // 테스트용 정적 파일
                        ).permitAll()
                        // 2. 나머지는 다 인증 필요
                        .anyRequest().authenticated()
                )
                // 3. JWT 필터 끼워넣기
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}