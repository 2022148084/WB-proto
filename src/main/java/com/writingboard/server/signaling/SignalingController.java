package com.writingboard.server.signaling;

import com.writingboard.server.signaling.dto.SignalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SignalingController {

    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * 클라이언트가 /pub/signal/{roomId} 로 메시지를 보내면 동작
     * 이 메시지를 /sub/room/{roomId} 를 구독하고 있는 사람들에게 그대로 전달
     */
    @MessageMapping("/signal/{roomId}")
    public void signal(@DestinationVariable String roomId, @Payload SignalMessage message) {
        log.info("Signal Type: {}, Sender: {}, Room: {}", message.getType(), message.getSender(), roomId);

        // 메시지 브로드캐스팅 (나를 포함한 방의 모든 사람에게 감 - 프론트에서 '나'는 필터링해야 함)
        // 실제로는 sender를 제외하고 보내는 로직을 짜기도 하지만, 구현 편의상 다 보내고 프론트에서 거르는게 빠름
        messagingTemplate.convertAndSend("/sub/room/" + roomId, message);
    }
}