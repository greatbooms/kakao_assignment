package com.eric.kakaopay.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomListDto {
    private String roomId;
    private Long count;

    @Builder
    public RoomListDto(String roomId, Long count) {
        this.roomId = roomId;
        this.count = count;
    }
}
