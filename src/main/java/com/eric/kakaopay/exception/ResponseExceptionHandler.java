package com.eric.kakaopay.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        String errorStr = "{\"errorCode\":\"9999\", \"errorMsg\":\"" + ex.getMessage().replace("\"","") + "\"}";
        log.error("{} | {}", request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json; charset=UTF-8").body(errorStr);
    }

    @ExceptionHandler(CommonException.class)
    public final ResponseEntity<?> handleCommonException(CommonException ex, WebRequest request) {
        log.error("{} | {}", request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json; charset=UTF-8").body(ex.getMessage());
    }
}
