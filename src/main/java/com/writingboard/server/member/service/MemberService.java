package com.writingboard.server.member.service;

import com.writingboard.server.global.auth.jwt.JwtProvider;
import com.writingboard.server.member.entity.Member;
import com.writingboard.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 1. 회원가입
    @Transactional
    public Long join(String loginId, String password, String nickname) {
        // 중복 체크
        if (memberRepository.existsByLoginId(loginId)) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화 (DB엔 암호문으로 저장됨)
        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .build();

        return memberRepository.save(member).getId();
    }

    // 2. 로그인 (성공하면 토큰 리턴)
    public String login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        // 암호화된 비밀번호와 입력한 비밀번호 비교
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 인증 성공! JWT 토큰 생성해서 줌
        return jwtProvider.createToken(member.getLoginId());
    }
}