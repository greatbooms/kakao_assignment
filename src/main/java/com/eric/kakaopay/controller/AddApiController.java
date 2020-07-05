package com.eric.kakaopay.controller;

import com.eric.kakaopay.service.InputService;
import com.eric.kakaopay.service.SpreadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping("/api/v2")
public class AddApiController {
    private final InputService inputService;
    private final SpreadService spreadService;

    @RequestMapping(value = "/roomList", method = RequestMethod.GET)
    public ResponseEntity<?> roomList() {
        return ResponseEntity.status(HttpStatus.OK).body(inputService.getRoomList());
    }
    @RequestMapping(value = "/roomUserList/{roomId}", method = RequestMethod.GET)
    public ResponseEntity<?> roomUserList(@PathVariable String roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(inputService.getRoomUserList(roomId));
    }

    @RequestMapping(value = "/roomSpreadInfoList/{roomId}", method = RequestMethod.GET)
    public ResponseEntity<?> roomSpreadInfoList(@PathVariable String roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(spreadService.getSpreadInfoList(roomId));
    }
}
