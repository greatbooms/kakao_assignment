package com.eric.kakaopay.service;

import com.eric.kakaopay.entity.RoomUserInfo;

public interface InputService {
    RoomUserInfo insertData(String roomId, Integer userId);
}
