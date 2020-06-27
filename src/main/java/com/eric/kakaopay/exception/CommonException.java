package com.eric.kakaopay.exception;

import com.eric.kakaopay.common.ErrorCode;

public class CommonException extends RuntimeException {
    public CommonException(ErrorCode errorCode) {
        super("{\"errorCode\":\"" + errorCode.getCode() + "\", \"errorMsg\":\"" + errorCode.getMsg() + "\"}");
    }
}
