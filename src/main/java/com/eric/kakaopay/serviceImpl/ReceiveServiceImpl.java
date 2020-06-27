package com.eric.kakaopay.serviceImpl;

import com.eric.kakaopay.common.ErrorCode;
import com.eric.kakaopay.dto.ReceiveDto;
import com.eric.kakaopay.entity.RoomUserInfo;
import com.eric.kakaopay.entity.SpreadInfo;
import com.eric.kakaopay.entity.SpreadReceiveInfo;
import com.eric.kakaopay.exception.CommonException;
import com.eric.kakaopay.repository.RoomUserInfoRepository;
import com.eric.kakaopay.repository.SpreadInfoRepository;
import com.eric.kakaopay.repository.SpreadReceiveInfoRepository;
import com.eric.kakaopay.service.ReceiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Service("receiveService")
@RequiredArgsConstructor
public class ReceiveServiceImpl implements ReceiveService {
    private final SpreadInfoRepository spreadInfoRepository;
    private final RoomUserInfoRepository roomUserInfoRepository;
    private final SpreadReceiveInfoRepository spreadReceiveInfoRepository;

    @Override
    @Transactional
    public ReceiveDto updateSpreadReceiveInfo(String roomId, Integer userId, String token) {
        RoomUserInfo ruInfo = roomUserInfoRepository.findByRoomIdAndUserId(roomId, userId);
        if (ruInfo == null) {
            throw new CommonException(ErrorCode.BAD_RECEIVE_REQUEST);
        }
        SpreadInfo sInfo = spreadInfoRepository.findByTokenJoinFetch(token);
        if (sInfo == null) {
            throw new CommonException(ErrorCode.NONE_SPREAD_REQUEST);
        }
        if (!sInfo.getRoomUserInfoKey().getRoomId().equals(roomId)) {
            throw new CommonException(ErrorCode.BAD_RECEIVE_REQUEST);
        }
        if (sInfo.getRoomUserInfoKey().getUserId() == userId) {
            throw new CommonException(ErrorCode.SELF_RECEIVE_REQUEST);
        }
        if (spreadReceiveInfoRepository.existsByRoomUserInfoKeyAndSpreadInfoKey(ruInfo, sInfo)) {
            throw new CommonException(ErrorCode.ALREADY_RECEIVE_REQUEST);
        }
        Timestamp ts = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(-10)); //10분전 보다 디비에 있는 시간이 클경우 10분내에 요청
        SpreadReceiveInfo srInfo = spreadReceiveInfoRepository.findFirstBySpreadInfoKeyAndRoomUserInfoKeyIsNullAndRegDateIsGreaterThanEqualOrderBySpreadReceiveInfoKeyAsc(sInfo, ts);
        if (srInfo == null) {
            throw new CommonException(ErrorCode.EXPIRED_RECEIVE_REQUEST);
        }
        srInfo.setUpdateInfo(ruInfo);
        spreadReceiveInfoRepository.save(srInfo);
        return ReceiveDto.builder()
                .spreadReceiveInfoKey(srInfo.getSpreadReceiveInfoKey())
                .spreadInfoKey(srInfo.getSpreadInfoKey().getSpreadInfoKey())
                .roomUserInfoKey(srInfo.getRoomUserInfoKey().getRoomUserInfoKey())
                .token(srInfo.getSpreadInfoKey().getToken())
                .receiveAmount(srInfo.getAmount())
                .receiveTime(srInfo.getReceiveTime())
                .build();
    }
}
