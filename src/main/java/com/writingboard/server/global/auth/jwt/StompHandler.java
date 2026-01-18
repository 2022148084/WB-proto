package com.writingboard.server.global.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // 1. 연결 요청(CONNECT) 시점에 토큰 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            log.info("STOMP Connect Authorization Header: {}", authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtProvider.validateToken(token)) {
                    log.info("STOMP JWT 인증 성공");
                    // 인증 성공 시 세션에 유저 정보 저장 가능
                    accessor.setUser(() -> jwtProvider.getLoginId(token));
                } else {
                    throw new AccessDeniedException("유효하지 않은 토큰입니다.");
                }
            } else {
                throw new AccessDeniedException("인증 헤더가 없습니다.");
            }
        }
        return message;
    }


    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();

        switch (accessor.getCommand()) {
            case CONNECT:
                log.info("유저 연결됨: SessionId = {}", sessionId);
                break;
            case DISCONNECT:
                log.info("유저 연결 종료: SessionId = {}", sessionId);
                // 여기서 방 퇴장 처리 로직 등을 수행
                break;
            default:
                break;
        }
    }
}