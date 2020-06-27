package com.eric.kakaopay.controller;

import com.eric.kakaopay.entity.SpreadInfo;
import com.eric.kakaopay.service.InputService;
import com.eric.kakaopay.service.InquiryService;
import com.eric.kakaopay.service.ReceiveService;
import com.eric.kakaopay.service.SpreadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping("/api/v1")
public class ApiController {
    private final InputService inputService;
    private final SpreadService spreadService;
    private final ReceiveService receiveService;
    private final InquiryService inquiryService;

    @RequestMapping(value = "/roomUserInput", method = RequestMethod.GET)
    public ResponseEntity<?> roomUserInput(@RequestHeader(value = "X-ROOM-ID") String roomId, @RequestHeader(value = "X-USER-ID") Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(inputService.insertData(roomId, userId));
    }

    @RequestMapping(value = "/spread", method = RequestMethod.POST)
    public ResponseEntity<?> spread(@RequestHeader(value = "X-ROOM-ID") String roomId, @RequestHeader(value = "X-USER-ID") Integer userId, @RequestBody SpreadInfo sInfo) {
        return ResponseEntity.status(HttpStatus.OK).body(spreadService.insertSpreadInfo(roomId, userId, sInfo));
    }

    @RequestMapping(value = "/spread/{token}", method = RequestMethod.PUT)
    public ResponseEntity<?> receive(@RequestHeader(value = "X-ROOM-ID") String roomId, @RequestHeader(value = "X-USER-ID") Integer userId, @PathVariable String token) {
        return ResponseEntity.status(HttpStatus.OK).body(receiveService.updateSpreadReceiveInfo(roomId, userId, token));
    }

    @RequestMapping(value = "/spread/{token}", method = RequestMethod.GET)
    public ResponseEntity<?> inquiry(@RequestHeader(value = "X-ROOM-ID") String roomId, @RequestHeader(value = "X-USER-ID") Integer userId, @PathVariable String token) {
        return ResponseEntity.status(HttpStatus.OK).body(inquiryService.inquirySpreadReceiveInfo(roomId, userId, token));
    }
}
