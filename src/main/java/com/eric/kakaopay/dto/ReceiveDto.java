package com.eric.kakaopay.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class ReceiveDto {
    private Integer spreadReceiveInfoKey;
    private Integer roomUserInfoKey;
    private Integer spreadInfoKey;
    private String token;
    private Long receiveAmount;
    private Timestamp receiveTime;

    @Builder
    public ReceiveDto(Integer spreadReceiveInfoKey, Integer roomUserInfoKey, Integer spreadInfoKey, String token, Long receiveAmount, Timestamp receiveTime) {
        this.spreadReceiveInfoKey = spreadReceiveInfoKey;
        this.roomUserInfoKey = roomUserInfoKey;
        this.spreadInfoKey = spreadInfoKey;
        this.token = token;
        this.receiveAmount = receiveAmount;
        this.receiveTime = receiveTime;
    }
}
