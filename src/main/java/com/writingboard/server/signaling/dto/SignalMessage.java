package com.writingboard.server.signaling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalMessage {
    private String type;     // "OFFER", "ANSWER", "ICE", "JOIN"
    private String roomId;   // 방 번호
    private String sender;   // 보낸 사람
    private String receiver; // ★ [필수] 받는 사람 (이게 있어야 1:1 매칭됨)
    private Object data;     // SDP 또는 ICE 정보
}