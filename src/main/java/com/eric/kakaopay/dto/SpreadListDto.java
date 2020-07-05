package com.eric.kakaopay.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
public class SpreadListDto {
    private Integer roomUserInfoKey;
    private String roomId;
    private int userId;
    private Integer spreadInfoKey;
    private String token;
    private Long totalAmount;
    private Integer totalUserCount;
    private String spreadTime;

    @Builder
    public SpreadListDto(Integer roomUserInfoKey, String roomId, int userId, Integer spreadInfoKey, String token, Long totalAmount, Integer totalUserCount, String spreadTime) {
        this.roomUserInfoKey = roomUserInfoKey;
        this.roomId = roomId;
        this.userId = userId;
        this.spreadInfoKey = spreadInfoKey;
        this.token = token;
        this.totalAmount = totalAmount;
        this.totalUserCount = totalUserCount;
        this.spreadTime = spreadTime;
    }
}
