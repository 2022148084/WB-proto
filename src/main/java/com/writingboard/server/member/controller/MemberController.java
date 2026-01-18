package com.writingboard.server.member.controller;

import com.writingboard.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 DTO
    public record JoinRequest(String loginId, String password, String nickname) {}
    // 로그인 DTO
    public record LoginRequest(String loginId, String password) {}

    // 회원가입 API
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequest dto) {
        memberService.join(dto.loginId(), dto.password(), dto.nickname());
        return ResponseEntity.ok("회원가입 성공!");
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest dto) {
        String token = memberService.login(dto.loginId(), dto.password());
        // JSON으로 { "token": "eyJhbG..." } 형태 반환
        return ResponseEntity.ok(Map.of("token", token));
    }
}