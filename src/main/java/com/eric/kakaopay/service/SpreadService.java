package com.eric.kakaopay.service;

import com.eric.kakaopay.dto.SpreadDto;
import com.eric.kakaopay.entity.SpreadInfo;

public interface SpreadService {
    SpreadDto insertSpreadInfo(String roomId, Integer userId, SpreadInfo sInfo);
}
