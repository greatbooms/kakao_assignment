package com.eric.kakaopay.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NO_HEADER_INFO(1001,"HTTP header에 X-USER-ID 또는 X-ROOM-ID가 존재하지 않습니다."),
    BAD_SPREAD_REQUEST(2001, "참여하지 않은 방에 뿌리기 요청을 할 수 없습니다."),
    TOO_SMALL_SPREAD_COUNT(2002, "뿌리기 최소 인원 수는 1명 이상입니다."),
    TOO_MANY_SPREAD_COUNT(2003, "현재 방의 총인원 수 보다 많은 뿌리기 요청을 할 수 없습니다."),
    TOO_SMALL_SPREAD_AMOUNT(2004,"현재 방의 총인원 수보다 적은 금액으로 뿌리기 요청을 할 수 없습니다."),
    BAD_RECEIVE_REQUEST(3001, "참여하지 않은 방에 뿌리기 요청을 받을 수 없습니다."),
    NONE_SPREAD_REQUEST(3002, "찾을수 없는 뿌리기 요청입니다."),
    SELF_RECEIVE_REQUEST(3003, "자신의 뿌리기 요청은 자신이 받을 수 없습니다."),
    ALREADY_RECEIVE_REQUEST(3004, "이미 받은 뿌리기 요청은 다시 받을 수 없습니다."),
    EXPIRED_RECEIVE_REQUEST(3005, "만료된 뿌리기 요청입니다."),
    EXPIRED_OR_NONE_INQUIRY(4001, "잘못된 요청 또는 만료된 요청입니다."),
    NOT_OWNER_INQUIRY(4002, "뿌리기 결과조회는 뿌리기 생성자만 가능합니다.");
    private final int code;
    private final String msg;
    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
