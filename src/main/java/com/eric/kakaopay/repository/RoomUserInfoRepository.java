package com.eric.kakaopay.repository;

import com.eric.kakaopay.dto.RoomListDto;
import com.eric.kakaopay.entity.RoomUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomUserInfoRepository extends JpaRepository<RoomUserInfo, Integer> {
    List<RoomUserInfo> findByRoomId(String room_id);
    RoomUserInfo findByRoomIdAndUserId(String room_id, Integer user_id);
    @Query(value =
            "SELECT " +
                    " new com.eric.kakaopay.dto.RoomListDto(rui.roomId, COUNT(rui.roomId))" +
                    "FROM RoomUserInfo rui " +
                    "GROUP BY rui.roomId"
    )
    List<RoomListDto> findUsingGroupRoomId();
}
