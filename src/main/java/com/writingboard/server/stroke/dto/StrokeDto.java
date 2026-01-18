package com.writingboard.server.stroke.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrokeDto {
    private String type;     // "START", "MOVE", "END" (그리기 상태)
    private double x;        // X 좌표
    private double y;        // Y 좌표
    private String color;    // 펜 색상 (예: "#ff0000")
    private double lineWidth; // 펜 굵기
    private String sender;   // 그리는 사람 (서버에서 채워줄 값)
}