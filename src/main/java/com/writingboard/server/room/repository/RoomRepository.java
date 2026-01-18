package com.writingboard.server.room.repository;

import com.writingboard.server.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // fetch join을 사용하여 Member(host)를 한 번의 쿼리로 가져옴 (N+1 방지)
    @Query("select r from Room r join fetch r.host order by r.id desc")
    List<Room> findAllWithHostByOrderByIdDesc();
}