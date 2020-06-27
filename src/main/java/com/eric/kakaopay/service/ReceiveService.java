package com.eric.kakaopay.service;

import com.eric.kakaopay.dto.ReceiveDto;

public interface ReceiveService {
    ReceiveDto updateSpreadReceiveInfo(String roomId, Integer userId, String token);
}
