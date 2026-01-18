package com.writingboard.server.signaling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalMessage {
    private String type;   // "OFFER", "ANSWER", "ICE", "TEXT" (테스트용)
    private String roomId; // 방 번호
    private String sender; // 보낸 사람
    private Object data;   // SDP 정보 or ICE Candidate or 채팅메시지
}