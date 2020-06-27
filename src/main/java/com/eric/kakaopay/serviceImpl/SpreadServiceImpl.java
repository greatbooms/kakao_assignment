package com.eric.kakaopay.serviceImpl;

import com.eric.kakaopay.common.ErrorCode;
import com.eric.kakaopay.dto.SpreadDto;
import com.eric.kakaopay.entity.RoomUserInfo;
import com.eric.kakaopay.entity.SpreadInfo;
import com.eric.kakaopay.entity.SpreadReceiveInfo;
import com.eric.kakaopay.exception.*;
import com.eric.kakaopay.repository.RoomUserInfoRepository;
import com.eric.kakaopay.repository.SpreadInfoRepository;
import com.eric.kakaopay.repository.SpreadReceiveInfoRepository;
import com.eric.kakaopay.service.SpreadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service("spreadService")
@RequiredArgsConstructor
public class SpreadServiceImpl implements SpreadService {
    private final SpreadInfoRepository spreadInfoRepository;
    private final RoomUserInfoRepository roomUserInfoRepository;
    private final SpreadReceiveInfoRepository spreadReceiveInfoRepository;

    @Override
    @Transactional
    public SpreadDto insertSpreadInfo(String roomId, Integer userId, SpreadInfo sInfo) {
        RoomUserInfo ruInfo = roomUserInfoRepository.findByRoomIdAndUserId(roomId, userId);
        if (ruInfo == null) {
            throw new CommonException(ErrorCode.BAD_SPREAD_REQUEST);
        }
        int joinRoomUserCount = roomUserInfoRepository.findByRoomId(roomId).size();
        if (sInfo.getTotalUserCount() < 1) {
            throw new CommonException(ErrorCode.TOO_SMALL_SPREAD_COUNT);
        }
        if (sInfo.getTotalUserCount() > joinRoomUserCount - 1) {
            throw new CommonException(ErrorCode.TOO_MANY_SPREAD_COUNT);
        }
        if (sInfo.getTotalAmount() < joinRoomUserCount) {
            throw new CommonException(ErrorCode.TOO_SMALL_SPREAD_AMOUNT);
        }
        String token;
        do {
            token = RandomStringUtils.randomAlphabetic(3);
        } while (spreadInfoRepository.existsByToken(token)); //랜덤생성한 토큰이 이미 데이터베이스에 저장되어있다면 다시 생성
        sInfo = SpreadInfo.builder()
                .roomUserInfoKey(ruInfo)
                .token(token)
                .totalAmount(sInfo.getTotalAmount())
                .totalUserCount(sInfo.getTotalUserCount())
                .build();
        spreadInfoRepository.save(sInfo);
        long remainAmount = sInfo.getTotalAmount(), receiveAmount;
        for (int i = 0; i < sInfo.getTotalUserCount(); i++) {
            if (i == sInfo.getTotalUserCount() - 1) { //마지막 랜덤 분배금 나눌 차례일 경우 남은 분배금 모두를 할당함
                receiveAmount = remainAmount;
            } else {
                receiveAmount = ThreadLocalRandom.current().nextLong(0, remainAmount / 2); //분배금의 격차를 줄이기위해 2로 나눔 조절 숫자가 높아질수록 편차가 적어짐

                remainAmount -= receiveAmount;
                if (remainAmount < 0) { //랜덤 분배금에서 남은 분배금을 뺐을 때 0이된다면 0으로 분배금 조정
                    remainAmount = 0;
                    receiveAmount = 0;
                }
            }
            log.debug("receiveAmount = " + receiveAmount);
            log.debug("remainAmount = " + remainAmount);
            SpreadReceiveInfo srInfo = SpreadReceiveInfo.builder()
                    .roomUserInfoKey(null)
                    .spreadInfoKey(sInfo)
                    .amount(receiveAmount)
                    .build();
            spreadReceiveInfoRepository.save(srInfo);
        }
        return SpreadDto.builder()
                .spreadInfoKey(sInfo.getSpreadInfoKey())
                .token(sInfo.getToken())
                .totalAmount(sInfo.getTotalAmount())
                .totalUserCount(sInfo.getTotalUserCount())
                .spreadTime(sInfo.getSpreadTime())
                .build();
    }
}
