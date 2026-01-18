package com.writingboard.server.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 헤더에서 "Authorization" 꺼내기
        String authHeader = request.getHeader("Authorization");

        // "Bearer " 로 시작하는 토큰이 있다면 검증
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 제거

            if (jwtProvider.validateToken(token)) {
                String loginId = jwtProvider.getLoginId(token);

                // 통과! (임시 인증 객체 생성)
                // 원래는 DB에서 UserDetails 조회해야 하지만, 성능을 위해 ID만으로 인증 처리 (선택사항)
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        loginId, null, Collections.emptyList());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}