package com.eric.kakaopay.service;

import com.eric.kakaopay.dto.SpreadDto;
import com.eric.kakaopay.dto.SpreadListDto;
import com.eric.kakaopay.entity.SpreadInfo;

import java.util.List;

public interface SpreadService {
    SpreadDto insertSpreadInfo(String roomId, Integer userId, SpreadInfo sInfo);

    List<SpreadListDto> getSpreadInfoList(String roomId);
}
