package com.eric.kakaopay.serviceImpl;

import com.eric.kakaopay.dto.RoomListDto;
import com.eric.kakaopay.entity.RoomUserInfo;
import com.eric.kakaopay.repository.RoomUserInfoRepository;
import com.eric.kakaopay.service.InputService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<RoomListDto> getRoomList() {
        return roomUserInfoRepository.findUsingGroupRoomId();
    }

    @Override
    public List<RoomUserInfo> getRoomUserList(String room_id) {
        return roomUserInfoRepository.findByRoomId(room_id);
    }
}
