package com.writingboard.server.room.service;

import com.writingboard.server.member.entity.Member;
import com.writingboard.server.member.repository.MemberRepository;
import com.writingboard.server.room.entity.Room;
import com.writingboard.server.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    // 1. 방 생성
    @Transactional
    public Long createRoom(String title, String hostLoginId) {
        Member host = memberRepository.findByLoginId(hostLoginId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        Room room = Room.builder()
                .title(title)
                .host(host)
                .build();

        return roomRepository.save(room).getId();
    }

    // 2. 방 목록 조회
    public List<Room> findAllRooms() {
        return roomRepository.findAllWithHostByOrderByIdDesc();
    }
}