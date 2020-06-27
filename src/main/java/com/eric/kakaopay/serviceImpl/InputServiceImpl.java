package com.eric.kakaopay.serviceImpl;

import com.eric.kakaopay.entity.RoomUserInfo;
import com.eric.kakaopay.repository.RoomUserInfoRepository;
import com.eric.kakaopay.service.InputService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("inputService")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InputServiceImpl implements InputService {
    private final RoomUserInfoRepository roomUserInfoRepository;

    @Override
    public RoomUserInfo insertData(String roomId, Integer userId) {
        RoomUserInfo ruInfo = RoomUserInfo.builder()
                .roomId(roomId)
                .userId(userId)
                .build();
        return roomUserInfoRepository.save(ruInfo);
    }
}
