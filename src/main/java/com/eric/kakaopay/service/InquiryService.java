package com.eric.kakaopay.service;

import com.eric.kakaopay.dto.InquiryDto;

public interface InquiryService {
    InquiryDto inquirySpreadReceiveInfo(String roomId, Integer userId, String token);
}
