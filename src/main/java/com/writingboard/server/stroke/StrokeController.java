package com.writingboard.server.stroke;

import com.writingboard.server.stroke.dto.StrokeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class StrokeController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/stroke/{roomId}")
    public void handleStroke(@DestinationVariable Long roomId, StrokeDto stroke, Principal principal) {
        // principal.getName()을 통해 누가 그렸는지 식별 가능
        stroke.setSender(principal.getName());
        // 해당 방을 구독 중인 유저들에게 전송
        // 구독 경로: /sub/rooms/{roomId}/stroke
        messagingTemplate.convertAndSend("/sub/rooms/" + roomId + "/stroke", stroke);
    }
}