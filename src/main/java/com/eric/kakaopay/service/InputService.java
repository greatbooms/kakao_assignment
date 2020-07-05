package com.eric.kakaopay.service;

import com.eric.kakaopay.dto.RoomListDto;
import com.eric.kakaopay.entity.RoomUserInfo;

import java.util.List;

public interface InputService {
    RoomUserInfo insertData(String roomId, Integer userId);

    List<RoomListDto> getRoomList();

    List<RoomUserInfo> getRoomUserList(String room_id);
}
