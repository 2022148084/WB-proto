package com.writingboard.server.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 protected로 막기 (안전성)
@EntityListeners(AuditingEntityListener.class)     // 생성시간 자동 기록
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;  // 로그인용 ID

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호 (BCrypt)

    @Column(nullable = false)
    private String nickname; // 화면에 보여줄 이름

    // 나중을 위해 Role(USER, ADMIN)을 넣을 수도 있지만 지금은 생략

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 생성자 대신 빌더 패턴 사용 (가독성 UP)
    @Builder
    public Member(String loginId, String password, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
    }
}