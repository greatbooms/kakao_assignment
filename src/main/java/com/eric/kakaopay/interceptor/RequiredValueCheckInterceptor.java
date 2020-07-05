package com.eric.kakaopay.interceptor;

import com.eric.kakaopay.common.ErrorCode;
import com.eric.kakaopay.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequiredValueCheckInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String roomId = request.getHeader("X-ROOM-ID");
        String userId = request.getHeader("X-USER-ID");
        log.debug("roomId = {}", roomId);
        log.debug("userId = {}", userId);
        log.debug(request.getRequestURI());

        if(StringUtils.isEmpty(roomId) || StringUtils.isEmpty(userId)){
            throw new CommonException(ErrorCode.NO_HEADER_INFO);
        }

        return super.preHandle(request, response, handler);
    }
}
