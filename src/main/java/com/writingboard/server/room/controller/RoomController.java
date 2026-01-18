package com.writingboard.server.room.controller;

import com.writingboard.server.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    public record RoomCreateRequest(String title) {}
    public record RoomResponse(Long roomId, String title, String hostNickname) {}

    // 1. 방 생성 (인증된 유저만 가능)
    @PostMapping
    public ResponseEntity<Long> createRoom(@RequestBody RoomCreateRequest dto, Principal principal) {
        // Principal에는 JWT에서 추출한 loginId가 들어있음
        Long roomId = roomService.createRoom(dto.title(), principal.getName());
        return ResponseEntity.ok(roomId);
    }

    // 2. 방 목록 조회
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms() {
        List<RoomResponse> rooms = roomService.findAllRooms().stream()
                .map(r -> new RoomResponse(r.getId(), r.getTitle(), r.getHost().getNickname()))
                .toList();
        return ResponseEntity.ok(rooms);
    }
}