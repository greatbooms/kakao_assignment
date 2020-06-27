package com.eric.kakaopay.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
public class InquiryDto {
    private Integer spreadInfoKey;
    private String token;
    private Long totalSpreadAmount;
    private Long totalReceiveAmount;
    private Integer totalSpreadUserCount;
    private Integer totalReceiveUserCount;
    private Timestamp spreadTime;
    List<ReceiveInfo> srInfoList;

    @Builder
    public InquiryDto(Integer spreadInfoKey, String token, Long totalSpreadAmount, Long totalReceiveAmount, Integer totalSpreadUserCount, Integer totalReceiveUserCount, Timestamp spreadTime, List<ReceiveInfo> srInfoList) {
        this.spreadInfoKey = spreadInfoKey;
        this.token = token;
        this.totalSpreadAmount = totalSpreadAmount;
        this.totalReceiveAmount = totalReceiveAmount;
        this.totalSpreadUserCount = totalSpreadUserCount;
        this.totalReceiveUserCount = totalReceiveUserCount;
        this.spreadTime = spreadTime;
        this.srInfoList = srInfoList;
    }

    @Setter
    @Getter
    public static class ReceiveInfo {
        private Integer userId;
        private Long receiveAmount;
        private Timestamp receiveTime;

        @Builder
        public ReceiveInfo(Integer userId, Long receiveAmount, Timestamp receiveTime) {
            this.userId = userId;
            this.receiveAmount = receiveAmount;
            this.receiveTime = receiveTime;
        }
    }
}
