package com.eric.kakaopay.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class SpreadDto {
    private Integer spreadInfoKey;
    private String token;
    private Long totalAmount;
    private Integer totalUserCount;
    private Timestamp spreadTime;

    @Builder
    public SpreadDto(Integer spreadInfoKey, String token, Long totalAmount, Integer totalUserCount, Timestamp spreadTime) {
        this.spreadInfoKey = spreadInfoKey;
        this.token = token;
        this.totalAmount = totalAmount;
        this.totalUserCount = totalUserCount;
        this.spreadTime = spreadTime;
    }
}
