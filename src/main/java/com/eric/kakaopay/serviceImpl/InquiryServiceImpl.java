package com.eric.kakaopay.serviceImpl;

import com.eric.kakaopay.common.ErrorCode;
import com.eric.kakaopay.dto.InquiryDto;
import com.eric.kakaopay.entity.SpreadReceiveInfo;
import com.eric.kakaopay.exception.CommonException;
import com.eric.kakaopay.repository.SpreadReceiveInfoRepository;
import com.eric.kakaopay.service.InquiryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("inquiryService")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InquiryServiceImpl implements InquiryService {
    private final SpreadReceiveInfoRepository spreadReceiveInfoRepository;

    @Override
    public InquiryDto inquirySpreadReceiveInfo(String roomId, Integer userId, String token) {
        Timestamp ts = new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(-7));
        List<SpreadReceiveInfo> srInfoList = spreadReceiveInfoRepository.findDataByToken(token, ts);
        if (srInfoList.size() == 0) {
            throw new CommonException(ErrorCode.EXPIRED_OR_NONE_INQUIRY);
        }
        if (userId != srInfoList.get(0).getSpreadInfoKey().getRoomUserInfoKey().getUserId()) {
            throw new CommonException(ErrorCode.NOT_OWNER_INQUIRY);
        }
        List<InquiryDto.ReceiveInfo> receiveInfoList = new ArrayList<>();
        int receiveCount = 0;
        long receiveAmount = 0L;
        for (SpreadReceiveInfo srInfo : srInfoList) {
            if (srInfo.getRoomUserInfoKey() != null) {
                InquiryDto.ReceiveInfo receiveInfo = InquiryDto.ReceiveInfo.builder()
                        .userId(srInfo.getRoomUserInfoKey().getUserId())
                        .receiveAmount(srInfo.getAmount())
                        .receiveTime(srInfo.getReceiveTime())
                        .build();
                receiveInfoList.add(receiveInfo);
                receiveAmount += srInfo.getAmount();
                receiveCount++;
            }
        }
        return InquiryDto.builder()
                .spreadInfoKey(srInfoList.get(0).getSpreadInfoKey().getSpreadInfoKey())
                .token(srInfoList.get(0).getSpreadInfoKey().getToken())
                .totalSpreadAmount(srInfoList.get(0).getSpreadInfoKey().getTotalAmount())
                .totalReceiveAmount(receiveAmount)
                .totalSpreadUserCount(srInfoList.get(0).getSpreadInfoKey().getTotalUserCount())
                .totalReceiveUserCount(receiveCount)
                .spreadTime(srInfoList.get(0).getSpreadInfoKey().getSpreadTime())
                .srInfoList(receiveInfoList)
                .build();
    }
}
