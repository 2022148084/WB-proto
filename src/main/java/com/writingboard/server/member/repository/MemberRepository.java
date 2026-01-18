package com.writingboard.server.member.repository;

import com.writingboard.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId); // 아이디로 회원 찾기
    boolean existsByLoginId(String loginId); // 중복 아이디 검사
}